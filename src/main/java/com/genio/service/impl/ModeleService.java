/*
 *  GenioService
 *  ------------
 *  Copyright (c) 2025
 *  Elsa HADJADJ <elsa.simha.hadjadj@gmail.com>
 *
 *  Licence sous Creative Commons CC-BY-NC-SA 4.0.
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 *  Dépôt GitHub (Back) :
 *  https://github.com/DUT-Info-Montreuil/GenioService
 */

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
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ModeleService {

    @Value("${modele.conventionServices.directory}")
    private String directoryPath;
    private static final String EXTENSION_DOCX = ".docx";

    private static final String MODEL_NOT_FOUND = "Modèle introuvable avec l'ID : ";
    private static final String FILENAME_REGEX = "^modeleConvention_\\d{4}\\.docx$";


    private final ConventionRepository conventionRepository;
    private final DataSource dataSource;
    private final DocxParser docxParser;
    private final ModeleRepository modeleRepository;
    private static final Logger log = LoggerFactory.getLogger(ModeleService.class);

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
        log.info("Démarrage de l'insertion des modèles depuis le répertoire : {}", directoryPath);
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(EXTENSION_DOCX));

        if (files == null || files.length == 0) {
            log.warn("Répertoire vide ou introuvable : {}", directoryPath);
            throw new EmptyDirectoryException("Le répertoire ne contient aucun fichier .docx.");
        }

        for (File file : files) {
            String modelName = file.getName();
            log.debug("Traitement du fichier : {}", modelName);

            if (!modelName.matches(FILENAME_REGEX)) {
                log.error("Nom de fichier invalide : {}", modelName);
                throw new InvalidFileFormatException("Format invalide : le fichier doit être nommé sous la forme 'modeleConvention_YYYY.docx'.");
            }

            insertModele(file, "2025");
        }

        log.info("Insertion depuis répertoire terminée.");
    }

    public void insertModele(File conventionServiceFile, String anneeUtilisateur) throws IOException {
        String modelName = conventionServiceFile.getName();
        log.info("Insertion du modèle : {} avec année par défaut '{}'", modelName, anneeUtilisateur);

        if (conventionServiceFile.length() == 0) {
            log.error("Le fichier {} est vide.", modelName);
            throw new EmptyFileException("Le fichier " + modelName + " est vide.");
        }

        byte[] fileBytes = Files.readAllBytes(conventionServiceFile.toPath());

        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);
        String modelYear = matcher.find() ? matcher.group(1) : anneeUtilisateur;

        if (modelYear == null || modelYear.isEmpty()) {
            log.error("Année introuvable dans le nom du fichier : {}", modelName);
            throw new InvalidFileFormatException("Le fichier " + modelName + " doit contenir une année (_YYYY.docx) ou l'année doit être précisée.");
        }

        if (dataSource == null) {
            log.error("DataSource non initialisée.");
            throw new IllegalStateException("DataSource is not initialized");
        }

        String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, modelName);
            preparedStatement.setString(2, modelYear);
            preparedStatement.setBytes(3, fileBytes);
            preparedStatement.executeUpdate();

            log.info("Modèle inséré avec succès : {}, année {}", modelName, modelYear);
        } catch (SQLException e) {
            log.error("Erreur SQL lors de l'insertion du modèle '{}': {}", modelName, e.getMessage(), e);
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
        log.info("Récupération de tous les modèles de convention non archivés...");

        List<Modele> modeles = modeleRepository.findAll()
                .stream()
                .filter(m -> !m.isArchived())
                .toList();

        if (modeles.isEmpty()) {
            log.warn("Aucun modèle de convention disponible.");
            throw new NoConventionServicesAvailableException("Aucun modèle de convention disponible.");
        }

        log.info("{} modèle(s) trouvé(s). Conversion en DTO...", modeles.size());

        return modeles.stream()
                .map(modele -> {
                    ModeleDTOForList dto = new ModeleDTOForList(
                            modele.getId(),
                            modele.getNom(),
                            generateDescription(modele),
                            "docx",
                            modele.getTitre()
                    );
                    if (modele.getDateDerniereModification() != null) {
                        dto.setDateDerniereModification(modele.getDateDerniereModification().toString());
                    }
                    return dto;
                }).toList();
    }

    private String generateDescription(Modele modele) {
        return "Modèle " + modele.getNom() + " de l'année " + modele.getAnnee();
    }

    public ModeleDTO getConventionServiceById(Long id) throws ConventionServiceNotFoundException {
        log.info("Recherche du modèle de convention avec l'ID {}", id);

        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Modèle de convention introuvable pour l'ID {}", id);
                    return new ConventionServiceNotFoundException(MODEL_NOT_FOUND + id);
                });

        log.info("Modèle trouvé : nom='{}', année='{}'", modele.getNom(), modele.getAnnee());

        return new ModeleDTO(
                modele.getId(),
                modele.getNom(),
                modele.getAnnee(),
                "docx",
                modele.getDateDerniereModification() != null ? modele.getDateDerniereModification().toString() : null,
                modele.getTitre(),
                modele.getDescriptionModification()
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
        for (String variable : foundVariables) {
            if (!variable.matches("^\\w+$")) {
                malformed.add(variable);
            }
        }
        return malformed;
    }

    public ModeleDTO createModelConvention(MultipartFile file, String annee, String titre)
            throws ModelConventionAlreadyExistsException, DatabaseInsertionException, IOException,
            MissingVariableException, InvalidFileFormatException {

        String originalFilename = file.getOriginalFilename();
        log.info("Début de création d’un modèle de convention. Fichier reçu : '{}', année : '{}', titre : '{}'",
                originalFilename, annee, titre);

        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(EXTENSION_DOCX)) {
            log.warn("Fichier non valide : extension incorrecte ou nom null.");
            throw new InvalidFileFormatException("Format non supporté, uniquement .docx accepté.");
        }

        if (annee == null || !annee.matches("^\\d{4}$")) {
            log.warn("Année invalide fournie : {}", annee);
            throw new InvalidFileFormatException("Année invalide. Format attendu : 4 chiffres (ex: 2025).");
        }

        String generatedFilename = "modeleConvention_" + annee + EXTENSION_DOCX;

        Optional<Modele> existingByName = modeleRepository.findFirstByNom(generatedFilename);
        if (existingByName.isPresent()) {
            Modele existing = existingByName.get();
            if (!existing.isArchived()) {
                log.warn("Un modèle pour l'année {} existe déjà et n’est pas archivé.", annee);
                throw new ModelConventionAlreadyExistsException("Un modèle pour l'année " + annee + " existe déjà.");
            } else {
                // Réactiver un modèle archivé
                log.info("Un modèle archivé pour l'année {} a été trouvé. Réactivation en cours...", annee);
                existing.setArchived(false);
                existing.setArchivedAt(null);
                existing.setFichierBinaire(file.getBytes());
                existing.setTitre((titre == null || titre.isBlank()) ? file.getOriginalFilename() : titre);
                existing.setDateDerniereModification(LocalDateTime.now());

                modeleRepository.save(existing);
                saveFileToDirectory(file, generatedFilename);

                log.info("Modèle archivé réactivé et mis à jour. ID : {}", existing.getId());
                return new ModeleDTO(
                        existing.getId(),
                        existing.getNom(),
                        existing.getAnnee(),
                        "docx",
                        existing.getDateDerniereModification().toString(),
                        titre,
                        null
                );
            }
        }

        List<String> foundVariables = docxParser.extractVariables(file);
        if (foundVariables == null || foundVariables.isEmpty()) {
            log.error("Aucune variable trouvée dans le fichier, possiblement non valide.");
            throw new InvalidFileFormatException("Le fichier ne semble pas être un modèle valide.");
        }

        List<String> missingVariables = findMissingVariables(foundVariables);
        List<String> malformedVariables = findMalformedVariables(foundVariables);

        if (!missingVariables.isEmpty() || !malformedVariables.isEmpty()) {
            log.warn("Variables manquantes ou mal formées détectées dans le modèle.");
            throw new MissingVariableException("Le fichier contient des erreurs de variables.");
        }

        byte[] fileBytes = file.getBytes();
        String fileHash = generateFileHash(fileBytes);

        Optional<Modele> existingByHash = modeleRepository.findFirstByFichierHash(fileHash);
        if (existingByHash.isPresent() && !existingByHash.get().isArchived()) {
            log.warn("Un modèle identique (hash) est déjà enregistré et actif.");
            throw new ModelConventionAlreadyExistsException("Ce fichier a déjà été ajouté.");
        }

        Modele modele = new Modele();
        modele.setNom(generatedFilename);
        modele.setAnnee(annee);
        modele.setFichierBinaire(fileBytes);
        modele.setFichierHash(fileHash);
        modele.setTitre((titre == null || titre.isBlank()) ? file.getOriginalFilename() : titre);

        modeleRepository.save(modele);
        saveFileToDirectory(file, generatedFilename);

        log.info("Nouveau modèle créé avec succès. ID : {}", modele.getId());

        return new ModeleDTO(
                modele.getId(),
                modele.getNom(),
                modele.getAnnee(),
                "docx",
                "Non spécifiée",
                titre,
                null
        );
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
            throws ModelConventionNotFoundException, ValidationException, IntegrityCheckFailedException {

        log.info("Début de la mise à jour du modèle de convention ID {}", id);

        Modele modele = modeleRepository.findById(id.longValue())
                .orElseThrow(() -> {
                    log.error("Modèle avec ID {} introuvable.", id);
                    return new ModelConventionNotFoundException("Modèle introuvable");
                });

        // Vérification de la cohérence de l’année
        if (modeleDTO.getAnnee() != null && !modeleDTO.getAnnee().equals(modele.getAnnee())) {
            log.warn("Tentative de modification de l’année pour le modèle ID {} — non autorisé.", id);
            throw new ValidationException("La modification de l'année d'un modèle existant n'est pas autorisée.");
        }

        // Validation du titre
        if (modeleDTO.getTitre() == null || modeleDTO.getTitre().trim().isEmpty()) {
            log.warn("Titre vide ou null fourni pour le modèle ID {}", id);
            throw new ValidationException("Le titre ne peut pas être vide.");
        }

        modele.setTitre(modeleDTO.getTitre());
        log.info("Titre mis à jour : '{}'", modeleDTO.getTitre());

        if (modeleDTO.getNom() != null) {
            modele.setNom(modeleDTO.getNom());
            log.info("Nom mis à jour : '{}'", modeleDTO.getNom());
        }

        if (modeleDTO.getDescriptionModification() != null) {
            modele.setDescriptionModification(modeleDTO.getDescriptionModification());
            log.info("Description de modification mise à jour.");
        }

        if (modeleDTO.getDateDerniereModification() != null) {
            try {
                modele.setDateDerniereModification(
                        LocalDateTime.parse(modeleDTO.getDateDerniereModification().replace("Z", ""))
                );
                log.info("Date de dernière modification mise à jour : {}", modeleDTO.getDateDerniereModification());
            } catch (Exception e) {
                log.error("Échec du parsing de la date : '{}'", modeleDTO.getDateDerniereModification(), e);
                throw new ValidationException("Format de date invalide : " + modeleDTO.getDateDerniereModification());
            }
        }

        modeleRepository.save(modele);
        log.info("Modèle ID {} mis à jour avec succès.", id);
    }


    private boolean checkIfModelIsInUse(Long modeleId) {
        return conventionRepository.countByModele_Id(modeleId) > 0;
    }

    public boolean isModelInUse(Long id) throws ModelConventionNotFoundException {
        log.info("Vérification de l'utilisation du modèle ID {}", id);

        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Modèle avec ID {} introuvable pour vérification d'utilisation.", id);
                    return new ModelConventionNotFoundException(MODEL_NOT_FOUND);
                });

        boolean inUse = checkIfModelIsInUse(modele.getId());
        log.info("Résultat de l'utilisation du modèle ID {} : {}", id, inUse ? "utilisé" : "non utilisé");
        return inUse;
    }

    public void archiveModelConvention(Long id)
            throws ModelConventionNotFoundException, ModelConventionInUseException {

        log.info("Début de la tentative d'archivage du modèle ID {}", id);

        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Modèle avec ID {} introuvable pour archivage.", id);
                    return new ModelConventionNotFoundException(MODEL_NOT_FOUND);
                });

        if (isModelInUse(modele.getId())) {
            log.warn("Impossible d'archiver le modèle ID {} car il est encore utilisé.", id);
            throw new ModelConventionInUseException("Le modèle est toujours utilisé et ne peut pas être archivé.");
        }

        modele.setArchived(true);
        modele.setArchivedAt(LocalDateTime.now());

        modeleRepository.save(modele);
        log.info("Modèle ID {} archivé avec succès. Nom : '{}'", id, modele.getNom());
    }

    public List<String> extractRawVariables(MultipartFile file) throws IOException {
        return docxParser.extractVariables(file);
    }

    public String generateFileHash(byte[] fileBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);
            return Base64.getEncoder().encodeToString(hashBytes);
        }catch (Exception e) {
            throw new FileHashGenerationException("Erreur lors du calcul du hash du fichier.", e);
        }
    }

    public void replaceModelFile(Long id, MultipartFile file) throws IOException, ModelConventionNotFoundException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new ModelConventionNotFoundException("Modèle introuvable"));

        modele.setFichierBinaire(file.getBytes());
        String newFileName = file.getOriginalFilename();
        if (newFileName != null && !newFileName.isBlank() && newFileName.endsWith(".docx")) {
            modele.setNom(newFileName);
        }

        modeleRepository.save(modele);
    }
}