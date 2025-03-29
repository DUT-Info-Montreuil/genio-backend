package com.genio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;

import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.genio.dto.TuteurDTO;
import com.genio.dto.EtudiantDTO;
import com.genio.dto.MaitreDeStageDTO;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.factory.ConventionFactory;
import com.genio.factory.TuteurFactory;
import com.genio.factory.EtudiantFactory;
import com.genio.factory.MaitreDeStageFactory;
import com.genio.model.*;
import com.genio.repository.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.output.ConventionBinaireRes;
import com.genio.service.GenioService;
import com.genio.service.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.genio.utils.ErrorMessages;

import java.io.File;
import java.nio.file.Files;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenioServiceImpl implements GenioService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private MaitreDeStageRepository maitreDeStageRepository;

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private ModeleRepository modeleRepository;

    @Autowired
    private HistorisationRepository historisationRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ErrorDetailsRepository errorDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(GenioServiceImpl.class);

    private final GenioServiceImpl self;

    private static final String STATUS_ECHEC = "ECHEC";

    public GenioServiceImpl(EtudiantRepository etudiantRepository,
                            MaitreDeStageRepository maitreDeStageRepository,
                            ConventionRepository conventionRepository,
                            ModeleRepository modeleRepository,
                            HistorisationRepository historisationRepository,
                            TuteurRepository tuteurRepository,
                            ErrorDetailsRepository errorDetailsRepository,
                            @Lazy GenioServiceImpl self) {
        this.etudiantRepository = etudiantRepository;
        this.maitreDeStageRepository = maitreDeStageRepository;
        this.conventionRepository = conventionRepository;
        this.modeleRepository = modeleRepository;
        this.historisationRepository = historisationRepository;
        this.tuteurRepository = tuteurRepository;
        this.errorDetailsRepository = errorDetailsRepository;
        this.self = self;
    }

    @Override
    @Transactional
    public ConventionBinaireRes generateConvention(ConventionServiceDTO input, String formatFichierOutput) {
        logger.info("Début de la génération de convention pour le modèle ID : {}", input.getModeleId());

        try {
            if (!"docx".equalsIgnoreCase(formatFichierOutput) && !"pdf".equalsIgnoreCase(formatFichierOutput)) {
                String message = "Erreur : format de fichier non supporté.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            if (input.getModeleId() == null) {
                String message = "Erreur : identifiant de modèle manquant.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            Modele modele = modeleRepository.findById(input.getModeleId())
                    .orElseThrow(() -> new ModelNotFoundException("Erreur : modèle introuvable avec l'ID " + input.getModeleId()));
            logger.info("Modèle récupéré avec ID: {}", modele.getId());

            if (modele.getFichierBinaire() == null && (modele.getNom() == null || modele.getNom().isBlank())) {
                String message = "Erreur : le modèle ne contient ni fichier binaire ni nom de fichier.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            if (modele.getNom() != null && !modele.getNom().endsWith(".docx")) {
                String message = "Erreur : le nom du modèle doit se terminer par .docx.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            Map<String, String> erreurs = validerDonnees(input);
            if (!erreurs.isEmpty()) {
                logger.warn("Des erreurs de validation ont été détectées : {}", erreurs);
                String erreursLisibles = erreurs.entrySet().stream()
                        .map(entry -> "Le champ '" + entry.getKey() + "' : " + entry.getValue())
                        .collect(Collectors.joining(", "));
                self.sauvegarderHistorisation(input, null, null, STATUS_ECHEC, erreurs);
                return new ConventionBinaireRes(false, null, "Les erreurs suivantes ont été détectées : " + erreursLisibles);
            }

            Etudiant etudiant = sauvegarderEtudiant(input.getEtudiant());
            MaitreDeStage maitreDeStage = sauvegarderMaitreDeStage(input.getMaitreDeStage());
            if (input.getTuteur() == null || input.getTuteur().getNom() == null || input.getTuteur().getPrenom() == null) {
                String message = "Le tuteur est manquant ou incomplet.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }
            Tuteur tuteur = sauvegarderTuteur(input.getTuteur());
            if (tuteur.getId() == null) {
                String message = "Erreur de persistance du tuteur.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            Convention convention = new Convention();
            convention.setEtudiant(etudiant);
            convention.setMaitreDeStage(maitreDeStage);
            convention.setTuteur(tuteur);
            convention.setModele(modele);
            conventionRepository.save(convention);

            Map<String, String> replacements = prepareReplacements(input, etudiant, maitreDeStage, tuteur, input.getStage().getAnneeStage());

            byte[] fichierBinaire;
            if (modele.getFichierBinaire() != null) {
                logger.info("Utilisation du modèle en BDD (fichier BLOB)");
                fichierBinaire = DocxGenerator.generateDocxFromTemplate(modele.getFichierBinaire(), replacements);
            } else {
                logger.info("Utilisation du modèle depuis les resources : {}", modele.getNom());
                String path = ResourceUtils.getFile("classpath:conventionServices/" + modele.getNom()).getPath();
                String outputFilePath = "output/conventionGenerée.docx";
                DocxGenerator.generateDocx(path, replacements, outputFilePath);
                fichierBinaire = Files.readAllBytes(new File(outputFilePath).toPath());
            }

            logger.info("Enregistrement de l'historisation pour la convention générée avec succès.");
            self.sauvegarderHistorisation(input, convention, fichierBinaire, "SUCCES", null);

            logger.info("La convention a été générée avec succès.");
            return new ConventionBinaireRes(true, fichierBinaire, null);

        } catch (ModelNotFoundException e) {
            logger.error("Modèle introuvable : {}", e.getMessage());
            self.sauvegarderHistorisation(input, null, null, STATUS_ECHEC, Map.of("modele", e.getMessage()));
            return new ConventionBinaireRes(false, null, e.getMessage());

        } catch (Exception e) {
            logger.error("Une erreur inattendue s'est produite : {}", e.getMessage(), e);
            self.sauvegarderHistorisation(input, null, null, STATUS_ECHEC, Map.of("technique", e.getMessage()));
            return new ConventionBinaireRes(false, null, "Erreur inattendue : contacter l’administrateur.");
        }
    }


    @Override
    public Map<String, String> validerDonnees(ConventionServiceDTO input) {

        logger.info("Début de la validation des données...");
        ValidationContext context = new ValidationContext();
        context.addStrategy(new EtudiantValidationStrategy());
        context.addStrategy(new MaitreDeStageValidationStrategy());
        context.addStrategy(new TuteurValidationStrategy());
        context.addStrategy(new OrganismeValidationStrategy());
        context.addStrategy(new StageValidationStrategy());

        Map<String, String> erreurs = context.executeValidations(input);

        if (input.getModeleId() == null) {
            erreurs.put("modeleId", ErrorMessages.MISSING_MODEL_ID);
        }

        if (input.getMaitreDeStage() == null) {
            erreurs.put("maitreDeStage", "Le champ 'maitreDeStage' est obligatoire.");
        }

        if (input.getOrganisme() == null || input.getOrganisme().getNom() == null) {
            erreurs.put("organisme", "Le nom de l'organisme est manquant.");
        }


        if (input.getStage() == null || input.getStage().getSujetDuStage() == null) {
            erreurs.put("stage", "Le sujet du stage est manquant.");
        }


        if (input.getTuteur() == null || input.getTuteur().getNom() == null) {
            erreurs.put("tuteur", "Le nom de l'enseignant est manquant.");
        }

        logger.info("Validation terminée avec {} erreur(s).", erreurs.size());
        return erreurs;
    }

    @Override
    public boolean modeleExiste(Long modeleId) {
        logger.info("Vérification de l'existence du modèle avec ID : {}", modeleId);
        boolean exists = modeleRepository.existsById(modeleId);
        logger.info("Le modèle {}.", exists ? "existe" : "n'existe pas");
        return exists;
    }

    @Transactional
    public void sauvegarderHistorisation(ConventionServiceDTO input, Convention convention, byte[] fichierBinaire, String status, Map<String, String> erreurs) {
        logger.info("Début de la sauvegarde de l'historisation avec le statut : {}", status);
        try {
            Historisation historisation = new Historisation();
            historisation.setConvention(convention);
            historisation.setStatus(status);
            historisation.setDetails(erreurs != null && !erreurs.isEmpty() ? "Des erreurs de validation ont été détectées." : "Aucune erreur détectée.");
            historisation.setFluxJsonBinaire(new ObjectMapper().writeValueAsBytes(input));
            historisation.setTimestamp();

            if (fichierBinaire != null) {
                historisation.setDocxBinaire(fichierBinaire);
            }

            logger.info("Sauvegarde de l'historisation...");
            historisationRepository.save(historisation);

            logger.info("Historisation sauvegardée avec succès.");

            if (erreurs != null && !erreurs.isEmpty()) {
                String messageErreur = erreurs.toString();
                if (messageErreur.length() > 255) {
                    messageErreur = messageErreur.substring(0, 255);
                }

                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setMessageErreur(messageErreur);
                StringBuilder champsManquants = new StringBuilder();
                erreurs.forEach((key, value) -> {
                    champsManquants.append(key).append(" ; ");
                });

                errorDetails.setChampsManquants(champsManquants.toString());
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
        logger.info("Étudiant sauvegardé avec succès : {}", savedEtudiant.getNom());
        return savedEtudiant;
    }

    private MaitreDeStage sauvegarderMaitreDeStage(MaitreDeStageDTO maitreDeStageDTO) {
        logger.info("Début de la sauvegarde du maitreDeStage : {}", maitreDeStageDTO.getNom());
        MaitreDeStage maitreDeStage = MaitreDeStageFactory.createMaitreDeStage(maitreDeStageDTO);
        MaitreDeStage savedMaitreDeStage = maitreDeStageRepository.save(maitreDeStage);
        logger.info("MaitreDeStage sauvegardé avec succès : {}", savedMaitreDeStage.getNom());
        return savedMaitreDeStage;
    }

    private Tuteur sauvegarderTuteur(TuteurDTO tuteurDTO) {
        logger.info("Début de la sauvegarde de tuteur : {}", tuteurDTO.getNom());
        if (tuteurDTO == null || tuteurDTO.getNom() == null || tuteurDTO.getPrenom() == null) {
            throw new IllegalArgumentException("Le tuteur est manquant ou incomplet.");
        }
        Tuteur tuteur = TuteurFactory.createTuteur(tuteurDTO);
        Tuteur savedTuteur = tuteurRepository.save(tuteur);
        logger.info("Tuteur sauvegardé avec succès : {}", savedTuteur.getNom());
        return savedTuteur;
    }

    private Convention sauvegarderConvention(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Tuteur tuteur,Modele modele) {
        logger.info("Début de la sauvegarde de la convention.");
        Convention convention = ConventionFactory.createConvention(input, etudiant, maitreDeStage,tuteur, modele);
        Convention savedConvention = conventionRepository.save(convention);
        logger.info("Convention sauvegardée avec succès pour l'année : {}", input.getStage().getAnneeStage());
        return savedConvention;
    }

    @Transactional(readOnly = true)
    public List<Modele> getModelesByAnnee(String annee) {
        List<Modele> modeles = modeleRepository.findByAnnee(annee);

        if (modeles.isEmpty()) {
            throw new ModelNotFoundException("Aucun modèle trouvé pour l'année : " + annee);
        }

        return modeles;
    }

    private byte[] genererFichierDocx(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Tuteur tuteur, String anneeStage) throws Exception {
        logger.info("Début de la génération du fichier DOCX pour la convention.");
        String conventionServicePath = ResourceUtils.getFile("classpath:conventionServices/modeleConvention_2024.docx").getPath();
        String outputFilePath = "output/conventionGenerée.docx";

        logger.info("Préparation des remplacements pour le fichier DOCX.");
        Map<String, String> replacements = prepareReplacements(input, etudiant, maitreDeStage, tuteur, anneeStage);

        logger.info("Génération du fichier DOCX à partir du conventionService : {}", conventionServicePath);
        DocxGenerator.generateDocx(conventionServicePath, replacements, outputFilePath);

        File generatedFile = new File(outputFilePath);
        if (!generatedFile.exists()) {
            logger.error("Erreur : Le fichier généré est introuvable.");
            throw new RuntimeException("Erreur : Le fichier généré est introuvable.");
        }

        logger.info("Fichier DOCX généré avec succès : {}", outputFilePath);
        return Files.readAllBytes(generatedFile.toPath());
    }

    private Map<String, String> prepareReplacements(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Tuteur tuteur, String anneeStage) {

        logger.info("Début de la préparation des remplacements pour le fichier DOCX.");
        Map<String, String> replacements = new HashMap<>();

        replacements.put("PRENOM_ETUDIANT", safeString(etudiant.getPrenom()));
        replacements.put("NOM_ETUDIANT1", safeString(etudiant.getNom()));
        replacements.put("ADR_ETUDIANT", safeString(input.getEtudiant().getAdresse()));
        replacements.put("TEL_ETUDIANT", safeString(input.getEtudiant().getTelephone()));
        replacements.put("MEL_ETUDIANT", safeString(etudiant.getEmail()));
        replacements.put("SEXE_ETUDIANT", safeString(input.getEtudiant().getSexe()));
        replacements.put("DATE_NAIS_ETUDIANT", safeString(input.getEtudiant().getDateNaissance()));
        replacements.put("NOM_CPAM", safeString(input.getEtudiant().getCpam()));

        replacements.put("PRENOM_ENCADRANT", safeString(maitreDeStage.getPrenom()));
        replacements.put("NOM_ENCADRANT", safeString(maitreDeStage.getNom()));
        replacements.put("FONCTION_ENCADRANT", safeString(input.getMaitreDeStage().getFonction()));
        replacements.put("TEL_ENCADRANT", safeString(input.getMaitreDeStage().getTelephone()));
        replacements.put("MEL_ENCADRANT", safeString(maitreDeStage.getEmail()));

        replacements.put("NOM_ORGANISME", safeString(input.getOrganisme().getNom()));
        replacements.put("ADR_ORGANISME", safeString(input.getOrganisme().getAdresse()));
        replacements.put("LIEU_DU_STAGE", safeString(input.getOrganisme().getLieuDuStage()));
        replacements.put("TEL_ORGANISME", safeString(input.getOrganisme().getTelephone()));
        replacements.put("MEL_ORGANISME", safeString(input.getOrganisme().getEmail()));
        replacements.put("NOM_REPRESENTANT_ORG", safeString(input.getOrganisme().getNomRepresentant()));
        replacements.put("QUAL_REPRESENTANT_ORG", safeString(input.getOrganisme().getQualiteRepresentant()));
        replacements.put("NOM_DU_SERVICE", safeString(input.getOrganisme().getNomDuService()));

        replacements.put("SUJET_DU_STAGE", safeString(input.getStage().getSujetDuStage()));
        replacements.put("DATE_DEBUT_STAGE", safeString(input.getStage().getDateDebutStage()));
        replacements.put("DATE_FIN_STAGE", safeString(input.getStage().getDateFinStage()));
        replacements.put("STA_DUREE", safeString(input.getStage().getDuree()));
        replacements.put("_STA_JOURS_TOT", String.valueOf(input.getStage().getJoursTot()));
        replacements.put("_STA_HEURES_TOT", String.valueOf(input.getStage().getHeuresTot()));
        replacements.put("STA_REMU_HOR", safeString(input.getStage().getRemunerationHoraire()));
        replacements.put("Stage_Professionnel", safeString(input.getStage().getSaeStageProfessionnel()));
        replacements.put("annee", safeString(input.getStage().getAnneeStage()));

        if (tuteur != null) {
            replacements.put("TUT_IUT", safeString(tuteur.getPrenom() + " " + tuteur.getNom()));
            replacements.put("TUT_IUT_MEL", safeString(tuteur.getEmail()));
        } else {
            replacements.put("TUT_IUT", "Non défini");
            replacements.put("TUT_IUT_MEL", "Non défini");
        }
        logger.info("Préparation des remplacements terminée.");
        return replacements;
    }

    private String safeString(String value) {
        return value != null ? value : "";
    }
}