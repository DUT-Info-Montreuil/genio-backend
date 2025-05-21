package com.genio.service.impl;

import com.genio.dto.outputmodeles.ModeleDTO;
import com.genio.dto.outputmodeles.ModeleDTOForList;
import com.genio.exception.business.*;
import com.genio.mapper.DocxParser;
import com.genio.model.Modele;
import com.genio.repository.ConventionRepository;
import com.genio.repository.ModeleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

@Service
public class ModeleService {

    @Value("${modele.conventionServices.directory}")
    private String directoryPath;

    private static final String MODEL_NOT_FOUND = "Modèle introuvable avec l'ID : ";
    private static final String FILENAME_REGEX = "^modeleConvention_\\d{4}\\.docx$";


    private final ConventionRepository conventionRepository;
    private final DataSource dataSource;
    private final DocxParser docxParser;
    private final ModeleRepository modeleRepository;

    public ModeleService(DataSource dataSource,
                         ConventionRepository conventionRepository,
                         ModeleRepository modeleRepository,
                         DocxParser docxParser) {
        this.dataSource = dataSource;
        this.conventionRepository = conventionRepository;
        this.modeleRepository = modeleRepository;
        this.docxParser = docxParser;
    }

    public void insertModeleFromDirectory() throws IOException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));

        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("Le répertoire ne contient aucun fichier .docx.");
        }

        for (File file : files) {
            String modelName = file.getName();
            if (!modelName.matches(FILENAME_REGEX)) {
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

        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);
        String modelYear = matcher.find() ? matcher.group(1) : anneeUtilisateur;

        if (modelYear == null || modelYear.isEmpty()) {
            throw new InvalidFileFormatException("Le fichier " + modelName + " doit contenir une année (_YYYY.docx) ou l'année doit être précisée.");
        }

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not initialized");
        }

        String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, modelName);
            preparedStatement.setString(2, modelYear);
            preparedStatement.setBytes(3, fileBytes);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseInsertionException("Erreur SQL lors de l'insertion du modèle '" + modelName + "' pour l'année " + modelYear, e);
        }
    }

    private static final List<String> EXPECTED_VARIABLES = List.of(
            "annee", "NOM_ORGANISME", "ADR_ORGANISME", "NOM_REPRESENTANT_ORG",
            "QUAL_REPRESENTANT_ORG", "NOM_DU_SERVICE", "TEL_ORGANISME", "MEL_ORGANISME",
            "LIEU_DU_STAGE", "NOM_ETUDIANT1", "PRENOM_ETUDIANT", "SEXE_ETUDIANT",
            "DATE_NAIS_ETUDIANT", "ADR_ETUDIANT", "TEL_ETUDIANT", "MEL_ETUDIANT",
            "SUJET_DU_STAGE", "DATE_DEBUT_STAGE", "DATE_FIN_STAGE", "STA_DUREE",
            "_STA_JOURS_TOT", "_STA_HEURES_TOT", "TUT_IUT", "TUT_IUT_MEL",
            "PRENOM_ENCADRANT", "NOM_ENCADRANT", "FONCTION_ENCADRANT",
            "TEL_ENCADRANT", "MEL_ENCADRANT", "NOM_CPAM", "Stage_Professionnel", "STA_REMU_HOR"
    );


    public static List<String> getExpectedVariables() {
        return Collections.unmodifiableList(EXPECTED_VARIABLES);
    }

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
                        "docx"))
                .toList();
    }

    private String generateDescription(Modele modele) {
        return "Modèle " + modele.getNom() + " de l'année " + modele.getAnnee();
    }

    public ModeleDTO getConventionServiceById(Long id) throws ConventionServiceNotFoundException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new ConventionServiceNotFoundException(MODEL_NOT_FOUND));

        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx", "Non spécifiée");
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
        for (String variable : foundVariables) {
            if (!variable.matches("^\\w+$")) {
                malformed.add(variable);
            }
        }
        return malformed;
    }

    public ModeleDTO createModelConvention(MultipartFile file, String annee)
            throws ModelConventionAlreadyExistsException, DatabaseInsertionException, IOException, MissingVariableException, InvalidFileFormatException {

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".docx")) {
            throw new InvalidFileFormatException("Format non supporté, uniquement .docx accepté.");
        }

        if (annee == null || !annee.matches("^\\d{4}$")) {
            throw new InvalidFileFormatException("Année invalide. Format attendu : 4 chiffres (ex: 2025).");
        }

        String generatedFilename = "modeleConvention_" + annee + ".docx";

        if (modeleRepository.findFirstByNom(generatedFilename).isPresent()) {
            throw new ModelConventionAlreadyExistsException("Un modèle pour l'année " + annee + " existe déjà.");
        }

        List<String> foundVariables = docxParser.extractVariables(file);

        if (foundVariables == null || foundVariables.isEmpty()) {
            throw new InvalidFileFormatException("Le fichier ne semble pas être un modèle valide. Aucun contenu exploitable détecté.");
        }

        List<String> missingVariables = findMissingVariables(foundVariables);
        List<String> malformedVariables = findMalformedVariables(foundVariables);

        if (!missingVariables.isEmpty() || !malformedVariables.isEmpty()) {
            throw new MissingVariableException("Le fichier semble être un modèle de convention, mais il contient des erreurs. Cliquez ci-dessous pour consulter les variables attendues.");
        }

        byte[] fileBytes = file.getBytes();
        Modele modele = new Modele();
        modele.setNom(generatedFilename);
        modele.setAnnee(annee);
        modele.setFichierBinaire(fileBytes);
        modeleRepository.save(modele);

        saveFileToDirectory(file, generatedFilename);

        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx", "Non spécifiée");
    }

    private String generateDetailedErrorMessage(List<String> missingVariables, List<String> malformedVariables) {
        StringBuilder message = new StringBuilder("Problèmes détectés dans le fichier : ");

        if (!missingVariables.isEmpty()) {
            message.append("-Variables manquantes : ").append(String.join(", ", missingVariables));
        }
        if (!malformedVariables.isEmpty()) {
            message.append(" Variables mal formatées : ").append(String.join(", ", malformedVariables));
        }

        return message.toString();
    }

    private void saveFileToDirectory(MultipartFile file, String originalFilename) throws IOException {
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(originalFilename);

        if (Files.exists(filePath)) {
            return;
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateModelConvention(Integer id, ModeleDTO modeleDTO)
            throws ModelConventionNotFoundException, ValidationException, UnauthorizedModificationException, IntegrityCheckFailedException {

        Modele modele = modeleRepository.findById(id.longValue())
                .orElseThrow(() -> new ModelConventionNotFoundException(MODEL_NOT_FOUND));

        if (modeleDTO.getNom() == null || modeleDTO.getNom().trim().isEmpty() || !modeleDTO.getNom().matches(FILENAME_REGEX)) {
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
        } catch (Exception e) {
            throw new DeletionFailedException("Échec de la suppression du modèle en raison d'une erreur technique.");
        }
    }

    public List<String> extractRawVariables(MultipartFile file) throws IOException {
        return docxParser.extractVariables(file);
    }
}