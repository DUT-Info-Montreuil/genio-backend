package com.genio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genio.dto.EnseignantDTO;
import com.genio.dto.EtudiantDTO;
import com.genio.dto.TuteurDTO;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.factory.ConventionFactory;
import com.genio.factory.EnseignantFactory;
import com.genio.factory.EtudiantFactory;
import com.genio.factory.TuteurFactory;
import com.genio.model.*;
import com.genio.repository.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.service.GenioService;
import com.genio.service.validation.*;
import com.genio.exception.business.InvalidDataException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.genio.utils.ErrorMessages;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenioServiceImpl implements GenioService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private ModeleRepository modeleRepository;

    @Autowired
    private HistorisationRepository historisationRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private ErrorDetailsRepository errorDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(GenioServiceImpl.class);

    @Override
    public ConventionBinaireRes generateConvention(ConventionServiceDTO input, String formatFichierOutput) {
        logger.info("Début de la génération de convention pour le modèle ID : {}", input.getModeleId());
        try {
            // Vérification de l'existence du modèle
            logger.debug("Vérification de l'existence du modèle...");
            Modele modele = modeleRepository.findById(input.getModeleId())
                    .orElseThrow(() -> {
                        logger.error("Modèle introuvable avec l'ID : {}", input.getModeleId());
                        return new ModelNotFoundException("Erreur : modèle introuvable avec l'ID " + input.getModeleId());
                    });
            // Validation des données
            logger.debug("Validation des données d'entrée...");
            Map<String, String> erreurs = validerDonnees(input);
            if (!erreurs.isEmpty()) {
                logger.warn("Des erreurs de validation ont été détectées : {}", erreurs);
                String erreursLisibles = erreurs.entrySet().stream()
                        .map(entry -> "Le champ '" + entry.getKey() + "' : " + entry.getValue())
                        .collect(Collectors.joining(", "));
                sauvegarderHistorisation(input, null, null, "ECHEC", erreurs);
                return new ConventionBinaireRes(false, null, "Les erreurs suivantes ont été détectées : " + erreursLisibles);
            }

            // Sauvegarde des entités
            logger.debug("Sauvegarde des entités dans la base de données...");
            Etudiant etudiant = sauvegarderEtudiant(input.getEtudiant());
            Tuteur tuteur = sauvegarderTuteur(input.getTuteur());
            Enseignant enseignant = sauvegarderEnseignant(input.getEnseignant());
            Convention convention = sauvegarderConvention(input, etudiant, tuteur, modele);

            // Vérification des objets
            if (etudiant == null || tuteur == null || modele == null) {
                throw new InvalidDataException("Les données d'entrée sont incomplètes ou invalides.");
            }

            // Génération du fichier
            logger.info("Génération du fichier binaire au format : {}", formatFichierOutput);
            byte[] fichierBinaire = genererFichierDocx(input, etudiant, tuteur, enseignant, convention);

            // Historisation du succès
            logger.info("Enregistrement de l'historisation pour la convention générée avec succès.");
            sauvegarderHistorisation(input, convention, fichierBinaire, "SUCCES", null);

            logger.info("La convention a été générée avec succès.");
            return new ConventionBinaireRes(true, fichierBinaire, null);

        } catch (ModelNotFoundException e) {
            logger.error("Modèle introuvable : {}", e.getMessage());
            sauvegarderHistorisation(input, null, null, "ECHEC", Map.of("modele", e.getMessage()));
            return new ConventionBinaireRes(false, null, e.getMessage());
        } catch (Exception e) {
            logger.error("Une erreur inattendue s'est produite : {}", e.getMessage());
            sauvegarderHistorisation(input, null, null, "ECHEC", Map.of("technique", e.getMessage()));
            return new ConventionBinaireRes(false, null, "Erreur inattendue : contacter l’administrateur.");
        }
    }

    @Override
    public Map<String, String> validerDonnees(ConventionServiceDTO input) {
        logger.debug("Début de la validation des données...");

        // Création du contexte et ajout des stratégies
        ValidationContext context = new ValidationContext();
        context.addStrategy(new EtudiantValidationStrategy());
        context.addStrategy(new TuteurValidationStrategy());
        context.addStrategy(new EnseignantValidationStrategy());
        context.addStrategy(new OrganismeValidationStrategy());
        context.addStrategy(new StageValidationStrategy());
        context.addStrategy(new ConventionValidationStrategy());

        // Exécution des validations via les stratégies
        Map<String, String> erreurs = context.executeValidations(input);

        // Validation spécifique au modèle
        if (input.getModeleId() == null) {
            erreurs.put("modeleId", ErrorMessages.MISSING_MODEL_ID);
        }

        logger.debug("Validation terminée avec {} erreur(s).", erreurs.size());
        return erreurs;
    }


    @Override
    public boolean modeleExiste(Long modeleId) {
        logger.debug("Vérification de l'existence du modèle avec ID : {}", modeleId);
        boolean exists = modeleRepository.existsById(modeleId);
        logger.info("Le modèle {}.", exists ? "existe" : "n'existe pas");
        return exists;
    }

    @Override
    @Transactional
    public void sauvegarderHistorisation(ConventionServiceDTO input, Convention convention, byte[] fichierBinaire, String status, Map<String, String> erreurs) {
        logger.info("Début de la sauvegarde de l'historisation avec le statut : {}", status);
        try {
            // Sérialisation du flux JSON en binaire
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] fluxJsonBinaire = objectMapper.writeValueAsBytes(input);

            // Détails
            String details;
            if (erreurs != null && !erreurs.isEmpty()) {
                details = "Des erreurs de validation ont été détectées.";
            } else {
                details = "Aucune erreur détectée.";
            }

            // Création de l'entité
            Historisation historisation = new Historisation();
            historisation.setConvention(convention);
            historisation.setStatus(status);
            historisation.setDetails(details);
            historisation.setFluxJsonBinaire(fluxJsonBinaire);

            if (fichierBinaire != null) {
                historisation.setDocxBinaire(fichierBinaire);
            }

            historisationRepository.save(historisation);

            logger.info("Historisation sauvegardée avec succès.");

            if (erreurs != null && !erreurs.isEmpty()) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setMessageErreur(erreurs.toString());
                errorDetails.setHistorisation(historisation);
                errorDetailsRepository.save(errorDetails);
                logger.warn("Des détails d'erreurs ont été enregistrés.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde de l'historisation : {}", e.getMessage());
        }
    }


    private Etudiant sauvegarderEtudiant(EtudiantDTO etudiantDTO) {
        logger.info("Début de la sauvegarde de l'étudiant : {}", etudiantDTO.getNom());
        Etudiant etudiant = EtudiantFactory.createEtudiant(etudiantDTO);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        if (savedEtudiant == null) {
            logger.error("Échec de la sauvegarde de l'étudiant.");
            throw new IllegalStateException("L'étudiant n'a pas été sauvegardé correctement.");
        }
        logger.info("Étudiant sauvegardé avec succès : {}", savedEtudiant.getNom());
        return savedEtudiant;
    }

    private Tuteur sauvegarderTuteur(TuteurDTO tuteurDTO) {
        logger.info("Début de la sauvegarde du tuteur : {}", tuteurDTO.getNom());
        Tuteur tuteur = TuteurFactory.createTuteur(tuteurDTO);
        Tuteur savedTuteur = tuteurRepository.save(tuteur);
        logger.info("Tuteur sauvegardé avec succès : {}", savedTuteur.getNom());
        return savedTuteur;
    }

    private Enseignant sauvegarderEnseignant(EnseignantDTO enseignantDTO) {
        logger.info("Début de la sauvegarde de l'enseignant : {}", enseignantDTO.getNom());
        Enseignant enseignant = EnseignantFactory.createEnseignant(enseignantDTO);
        Enseignant savedEnseignant = enseignantRepository.save(enseignant);
        logger.info("Enseignant sauvegardé avec succès : {}", savedEnseignant.getNom());
        return savedEnseignant;
    }

    private Convention sauvegarderConvention(ConventionServiceDTO input, Etudiant etudiant, Tuteur tuteur, Modele modele) {
        logger.info("Début de la sauvegarde de la convention pour l'année : {}", input.getAnnee());
        Convention convention = ConventionFactory.createConvention(input, etudiant, tuteur, modele);
        Convention savedConvention = conventionRepository.save(convention);
        logger.info("Convention sauvegardée avec succès pour l'année : {}", savedConvention.getAnnee());
        return savedConvention;
    }


    private byte[] genererFichierDocx(ConventionServiceDTO input, Etudiant etudiant, Tuteur tuteur, Enseignant enseignant, Convention convention) throws Exception {
        logger.info("Début de la génération du fichier DOCX pour la convention.");
        String templatePath = ResourceUtils.getFile("classpath:templates/modeleConvention.docx").getPath();
        String outputFilePath = "output/conventionGenerée.docx";

        logger.debug("Préparation des remplacements pour le fichier DOCX.");
        Map<String, String> replacements = prepareReplacements(input, etudiant, tuteur, enseignant, convention);

        logger.debug("Génération du fichier DOCX à partir du template : {}", templatePath);
        DocxGenerator.generateDocx(templatePath, replacements, outputFilePath);

        File generatedFile = new File(outputFilePath);
        if (!generatedFile.exists()) {
            logger.error("Erreur : Le fichier généré est introuvable.");
            throw new RuntimeException("Erreur : Le fichier généré est introuvable.");
        }

        logger.info("Fichier DOCX généré avec succès : {}", outputFilePath);
        return Files.readAllBytes(generatedFile.toPath());
    }


    private Map<String, String> prepareReplacements(ConventionServiceDTO input, Etudiant etudiant, Tuteur tuteur, Enseignant enseignant, Convention convention) {
        logger.debug("Début de la préparation des remplacements pour le fichier DOCX.");
        Map<String, String> replacements = new HashMap<>();

        // Remplacements pour l'étudiant
        replacements.put("PRENOM_ETUDIANT", safeString(etudiant.getPrenom()));
        replacements.put("NOM_ETUDIANT1", safeString(etudiant.getNom()));
        replacements.put("ADR_ETUDIANT", safeString(input.getEtudiant().getAdresse()));
        replacements.put("TEL_ETUDIANT", safeString(input.getEtudiant().getTelephone()));
        replacements.put("MEL_ETUDIANT", safeString(etudiant.getEmail()));
        replacements.put("SEXE_ETUDIANT", safeString(input.getEtudiant().getSexe()));
        replacements.put("DATE_NAIS_ETUDIANT", safeString(input.getEtudiant().getDateNaissance()));

        // Remplacements pour le tuteur
        replacements.put("PRENOM_ENCADRANT", safeString(tuteur.getPrenom()));
        replacements.put("NOM_ENCADRANT", safeString(tuteur.getNom()));
        replacements.put("FONCTION_ENCADRANT", safeString(input.getTuteur().getFonction()));
        replacements.put("TEL_ENCADRANT", safeString(input.getTuteur().getTelephone()));
        replacements.put("MEL_ENCADRANT", safeString(tuteur.getEmail()));

        // Remplacements pour l'organisme
        replacements.put("NOM_ORGANISME", safeString(input.getOrganisme().getNom()));
        replacements.put("ADR_ORGANISME", safeString(input.getOrganisme().getAdresse()));
        replacements.put("LIEU_DU_STAGE", safeString(input.getOrganisme().getLieuDuStage()));
        replacements.put("TEL_ORGANISME", safeString(input.getOrganisme().getTelephone()));
        replacements.put("MEL_ORGANISME", safeString(input.getOrganisme().getEmail()));
        replacements.put("NOM_REPRESENTANT_ORG", safeString(input.getOrganisme().getNomRepresentant()));
        replacements.put("QUAL_REPRESENTANT_ORG", safeString(input.getOrganisme().getQualiteRepresentant()));
        replacements.put("NOM_DU_SERVICE", safeString(input.getOrganisme().getNomDuService()));

        // Remplacements pour le stage
        replacements.put("SUJET_DU_STAGE", safeString(input.getStage().getSujetDuStage()));
        replacements.put("DATE_DÉBUT_STAGE", safeString(input.getStage().getDateDebutStage()));
        replacements.put("DATE_FIN_STAGE", safeString(input.getStage().getDateFinStage()));
        replacements.put("STA_DUREE", safeString(input.getStage().getDuree()));
        replacements.put("_STA_JOURS_TOT", String.valueOf(input.getStage().getJoursTot()));
        replacements.put("_STA_HEURES_TOT", String.valueOf(input.getStage().getHeuresTot()));
        replacements.put("STA_REMU_HOR", safeString(input.getStage().getRemunerationHoraire()));
        replacements.put("Stage_Professionnel", safeString(input.getStage().getSaeStageProfessionnel()));

        // Remplacements pour l'enseignant
        if (enseignant != null) {
            replacements.put("TUT_IUT", safeString(enseignant.getPrenom() + " " + enseignant.getNom()));
            replacements.put("TUT_IUT_MEL", safeString(enseignant.getEmail()));
        } else {
            replacements.put("TUT_IUT", "Non défini");
            replacements.put("TUT_IUT_MEL", "Non défini");
        }

        // Remplacements pour la convention
        replacements.put("annee", safeString(convention.getAnnee()));
        replacements.put("NOM_CPAM", safeString(input.getConvention().getCpam()));

        logger.debug("Préparation des remplacements terminée.");
        return replacements;
    }

    // Méthode pour retourner une chaîne vide si la valeur est null
    private String safeString(String value) {
        return value != null ? value : "";
    }
}