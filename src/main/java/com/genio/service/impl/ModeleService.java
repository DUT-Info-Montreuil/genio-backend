package com.genio.service.impl;
import com.genio.dto.outputmodeles.ModeleDTO;
import com.genio.dto.outputmodeles.ModeleDTOForList;
import com.genio.exception.business.*;
import com.genio.mapper.DocxParser;
import com.genio.model.Modele;
import com.genio.repository.ConventionRepository;
import com.genio.repository.ModeleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.genio.exception.business.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;
import com.genio.exception.business.ModelConventionNotFoundException;
import com.genio.exception.business.UnauthorizedModificationException;
import com.genio.exception.business.IntegrityCheckFailedException;


@Service
public class ModeleService {



    private static final Logger logger = LoggerFactory.getLogger(ModeleService.class);

    @Value("${modele.conventionServices.directory}")
    private String directoryPath;

    private static final String MODEL_NOT_FOUND = "Modèle introuvable avec l'ID : ";


    private ConventionRepository conventionRepository;

    private final DataSource dataSource;

    private DocxParser docxParser;

    private ModeleRepository modeleRepository;

    public ModeleService(DataSource dataSource,
                         ConventionRepository conventionRepository,
                         ModeleRepository modeleRepository,
                         DocxParser docxParser) {
        this.dataSource = dataSource;
        this.conventionRepository = conventionRepository;
        this.modeleRepository = modeleRepository;
        this.docxParser = docxParser;
    }

    private static final String FILENAME_REGEX = "^modeleConvention_\\d{4}\\.docx$";

    public void insertModeleFromDirectory() throws IOException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));

        if (files == null || files.length == 0) {
            logger.warn("Aucun fichier .docx trouvé dans le répertoire.");
            throw new EmptyDirectoryException("Le répertoire ne contient aucun fichier .docx.");
        }

        for (File file : files) {
            String modelName = file.getName();
            if (!modelName.matches(FILENAME_REGEX )) {
                logger.error("Erreur : Le fichier {} ne respecte pas le format attendu 'modeleConvention_YYYY.docx'.", modelName);
                throw new InvalidFileFormatException("Format invalide : le fichier doit être nommé sous la forme 'modeleConvention_YYYY.docx'.");
            }

            insertModele(file, "2025");
        }
    }

    public void insertModele(File conventionServiceFile, String anneeUtilisateur) throws IOException {
        if (conventionServiceFile.length() == 0) {
            throw new EmptyFileException("Le fichier " + conventionServiceFile.getName() + " est vide.");
        }

        byte[] fileBytes = Files.readAllBytes(conventionServiceFile.toPath());
        String modelName = conventionServiceFile.getName();

        logger.info("Nom du fichier : {}", modelName);
        logger.info("Taille : {} octets", conventionServiceFile.length());
        logger.info("Date de création : {}", new java.util.Date(conventionServiceFile.lastModified()));

        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);
        String modelYear = matcher.find() ? matcher.group(1) : anneeUtilisateur;

        if (modelYear == null || modelYear.isEmpty()) {
            logger.error("Erreur : Impossible de déterminer l'année.");
            throw new InvalidFileFormatException("Le fichier " + modelName + " doit contenir une année (_YYYY.docx) ou l'année doit être précisée.");
        }

        logger.info("Année utilisée : {}", modelYear);

        if (dataSource != null) {
            String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, modelName);
                preparedStatement.setString(2, modelYear);
                preparedStatement.setBytes(3, fileBytes);
                preparedStatement.executeUpdate();
                logger.info("Modèle inséré avec succès : {}", modelName);
            } catch (SQLException e) {
                logger.error("Erreur SQL lors de l'insertion du modèle {}. Détails : {}", modelName, e.getMessage());
                throw new DatabaseInsertionException("Erreur lors de l'insertion du modèle " + modelName + " dans la base de données.", e);
            }
        } else {
            logger.warn("La base de données n'est pas configurée. Aucun modèle n'a été inséré.");
        }
    }

    private static final List<String> EXPECTED_VARIABLES = Arrays.asList(
            "annee", "NOM_ORGANISME", "ADR_ORGANISME", "NOM_REPRESENTANT_ORG",
            "QUAL_REPRESENTANT_ORG", "NOM_DU_SERVICE", "TEL_ORGANISME", "MEL_ORGANISME",
            "LIEU_DU_STAGE", "NOM_ETUDIANT1", "PRENOM_ETUDIANT", "SEXE_ETUDIANT",
            "DATE_NAIS_ETUDIANT", "ADR_ETUDIANT", "TEL_ETUDIANT", "MEL_ETUDIANT",
            "SUJET_DU_STAGE", "DATE_DEBUT_STAGE", "DATE_FIN_STAGE", "STA_DUREE",
            "_STA_JOURS_TOT", "_STA_HEURES_TOT", "TUT_IUT", "TUT_IUT_MEL",
            "PRENOM_ENCADRANT", "NOM_ENCADRANT", "FONCTION_ENCADRANT",
            "TEL_ENCADRANT", "MEL_ENCADRANT", "NOM_CPAM", "Stage_Professionnel", "STA_REMU_HOR"
    );





    public List<ModeleDTOForList> getAllConventionServices() throws NoConventionServicesAvailableException {
        List<Modele> modeles = modeleRepository.findAll();

        if (modeles.isEmpty()) {
            throw new NoConventionServicesAvailableException("Aucun modèle de convention disponible.");
        }

        return modeles.stream()
                .map(modele -> new ModeleDTOForList(
                        modele.getId(),
                        modele.getNom(),
                        generateDescription(modele),
                        "docx"
                ))
                .toList();
    }

    private String generateDescription(Modele modele) {
        return "Modèle " + modele.getNom() + " de l'année " + modele.getAnnee();
    }



    public ModeleDTO getConventionServiceById(Long id) throws ConventionServiceNotFoundException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new ConventionServiceNotFoundException(MODEL_NOT_FOUND));

        return new ModeleDTO(
                modele.getId(),
                modele.getNom(),
                modele.getAnnee(),
                "docx",
                "Non spécifiée"
        );
    }


    private List<String> findMissingVariables(List<String> foundVariables) {
        List<String> missing = new ArrayList<>();
        for (String expected : EXPECTED_VARIABLES) {
            if (!foundVariables.contains(expected)) {
                missing.add(expected);
            }
        }
        return missing;
    }

    private List<String> findMalformedVariables(List<String> foundVariables) {
        List<String> malformed = new ArrayList<>();
        for (String variable: foundVariables) {
            if (!variable.matches("^\\w+$")) {
                malformed.add(variable);
            }
        }
        return malformed;
    }

    public ModeleDTO createModelConvention(String nom,  MultipartFile file)
            throws ModelConventionAlreadyExistsException, DatabaseInsertionException, IOException, MissingVariableException {

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(FILENAME_REGEX )) {
            logger.error("Erreur : Le fichier '{}' ne respecte pas le format attendu 'modeleConvention_YYYY.docx'.", originalFilename);
            throw new InvalidFileFormatException("Format invalide : le fichier doit être nommé sous la forme 'modeleConvention_YYYY.docx'.");
        }

        if (modeleRepository.findFirstByNom(originalFilename).isPresent()) {
            throw new ModelConventionAlreadyExistsException("Un modèle avec ce fichier existe déjà");
        }

        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(originalFilename);
        String modelYear = matcher.find() ? matcher.group(1) : "2025";

        List<String> foundVariables = docxParser.extractVariables(file);
        List<String> missingVariables = findMissingVariables(foundVariables);
        List<String> malformedVariables = findMalformedVariables(foundVariables);

        if (!missingVariables.isEmpty() || !malformedVariables.isEmpty()) {
            String errorMessage = generateDetailedErrorMessage(missingVariables, malformedVariables);
            logger.error("Erreur de validation : {}", errorMessage);
            throw new MissingVariableException(errorMessage);
        }

        byte[] fileBytes = file.getBytes();
        Modele modele = new Modele();
        modele.setNom(originalFilename);
        modele.setAnnee(modelYear);
        modele.setFichierBinaire(fileBytes);
        modeleRepository.save(modele);

        saveFileToDirectory(file, originalFilename);

        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx",  "Non spécifiée");
    }


    private String generateDetailedErrorMessage(List<String> missingVariables, List<String> malformedVariables) {
        StringBuilder message = new StringBuilder("Problèmes détectés dans le fichier : ");

        if (!missingVariables.isEmpty()) {
            message.append("-Variables manquantes : ").append(String.join(", ", missingVariables));
        }
        if (!malformedVariables.isEmpty()) {
            message.append("Variables mal formatées : ").append(String.join(", ", malformedVariables));
        }

        logger.debug("Message d'erreur détaillé : {}", message);
        return message.toString();
    }

    private void saveFileToDirectory(MultipartFile file, String originalFilename) throws IOException {
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(originalFilename);

        if (Files.exists(filePath)) {
            logger.warn("Un fichier '{}' existe déjà dans le répertoire. Il ne sera pas écrasé.", originalFilename);
            return;
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Fichier enregistré avec succès sous : {}", filePath);
    }

    public void updateModelConvention(Integer id, ModeleDTO modeleDTO)
            throws ModelConventionNotFoundException, ValidationException, UnauthorizedModificationException, IntegrityCheckFailedException {

        Modele modele = modeleRepository.findById(id.longValue())
                .orElseThrow(() -> new ModelConventionNotFoundException(MODEL_NOT_FOUND));

        if (modeleDTO.getNom() == null || modeleDTO.getNom().trim().isEmpty() || !modeleDTO.getNom().matches(FILENAME_REGEX )) {
            throw new ValidationException("Le nom du modèle est invalide ou ne respecte pas le format 'modeleConvention_YYYY.docx'.");
        }
        if (modeleDTO.getAnnee() == null || !modeleDTO.getAnnee().matches("^\\d{4}$")) {
            throw new ValidationException("L'année fournie est invalide.");
        }

        if (!modele.getAnnee().equals(modeleDTO.getAnnee())) {
            throw new UnauthorizedModificationException("La modification de l'année d'un modèle existant n'est pas autorisée.");
        }

        if (modeleRepository.findFirstByNom(modeleDTO.getNom()).isPresent() && !modele.getNom().equals(modeleDTO.getNom())) {
            throw new IntegrityCheckFailedException("Un modèle avec ce nom existe déjà.");
        }

        modele.setNom(modeleDTO.getNom());
        modele.setAnnee(modeleDTO.getAnnee());
        modeleRepository.save(modele);

        logger.info("Modèle mis à jour avec succès : {}", modeleDTO.getNom());
    }

    private boolean checkIfModelIsInUse(Long modeleId) {
        return conventionRepository.countByModele_Id(modeleId) > 0;
    }

    public boolean isModelInUse(Long id) throws ModelConventionNotFoundException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new ModelConventionNotFoundException(MODEL_NOT_FOUND));

        return checkIfModelIsInUse(modele.getId());
    }

    public void deleteModelConvention(Long id)
            throws ModelConventionNotFoundException, ModelConventionInUseException, DeletionFailedException {

        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new ModelConventionNotFoundException(MODEL_NOT_FOUND));

        if (isModelInUse(modele.getId())) {
            throw new ModelConventionInUseException("Le modèle est toujours utilisé et ne peut pas être supprimé.");
        }

        try {
            modeleRepository.delete(modele);
            logger.info("Modèle supprimé avec succès : {}", modele.getNom());
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du modèle {} : {}", modele.getNom(), e.getMessage());
            throw new DeletionFailedException("Échec de la suppression du modèle en raison d'une erreur technique.");
        }
    }
}