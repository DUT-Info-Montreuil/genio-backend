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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.service.impl;

import com.genio.utils.ErreurType;
import com.genio.config.ErreurDetaillee;
import org.springframework.transaction.annotation.Transactional;
import com.genio.dto.TuteurDTO;
import com.genio.dto.EtudiantDTO;
import com.genio.dto.MaitreDeStageDTO;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.factory.TuteurFactory;
import com.genio.factory.EtudiantFactory;
import com.genio.factory.MaitreDeStageFactory;
import com.genio.model.*;
import com.genio.repository.*;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.service.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.genio.config.ErrorMessages;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenioServiceImpl implements GenioService {

    private final ModeleRepository modeleRepository;
    private final TuteurRepository tuteurRepository;
    private static final Logger logger = LoggerFactory.getLogger(GenioServiceImpl.class);

    private final DocxGenerator docxGenerator;
    private static final String STATUS_ECHEC = "ECHEC";

    private final EtudiantRepository etudiantRepository;
    private final MaitreDeStageRepository maitreDeStageRepository;
    private final ConventionRepository conventionRepository;
    private final HistorisationService historisationService;

    public GenioServiceImpl(EtudiantRepository etudiantRepository,
                            MaitreDeStageRepository maitreDeStageRepository,
                            ConventionRepository conventionRepository,
                            ModeleRepository modeleRepository,
                            TuteurRepository tuteurRepository,
                            DocxGenerator docxGenerator,
                            HistorisationService historisationService) {
        this.etudiantRepository = etudiantRepository;
        this.maitreDeStageRepository = maitreDeStageRepository;
        this.conventionRepository = conventionRepository;
        this.modeleRepository = modeleRepository;
        this.tuteurRepository = tuteurRepository;
        this.docxGenerator = docxGenerator;
        this.historisationService = historisationService;
    }

    public boolean isFormatValide(String format) {
        return "docx".equalsIgnoreCase(format) || "pdf".equalsIgnoreCase(format);
    }

    public ConventionBinaireRes verifierModele(Modele modele) {
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

        return null;
    }

    public boolean isTuteurIncomplet(TuteurDTO tuteur) {
        return tuteur == null || tuteur.getNom() == null || tuteur.getPrenom() == null;
    }

    @Override
    @Transactional
    public ConventionBinaireRes generateConvention(ConventionServiceDTO input, String formatFichierOutput) {
        logger.info("Début de la génération de convention pour le modèle ID : {}", input.getModeleId());

        try {
            if (!isFormatValide(formatFichierOutput)) {
                String message = "Erreur : format de fichier non supporté.";
                logger.error(message);
                historisationService.sauvegarderHistorisation(
                        input,
                        null,
                        null,
                        STATUS_ECHEC,
                        List.of(new ErreurDetaillee("flux", message, ErreurType.FLUX))
                );
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

            if (modele.isArchived()) {
                String message = "Erreur : le modèle demandé est archivé et ne peut pas être utilisé.";
                logger.warn(message);
                historisationService.sauvegarderHistorisation(
                        input,
                        null,
                        null,
                        STATUS_ECHEC,
                        List.of(new ErreurDetaillee("modele", message, ErreurType.FLUX))
                );
                return new ConventionBinaireRes(false, null, message);
            }

            ConventionBinaireRes erreurModele = verifierModele(modele);
            if (erreurModele != null) {
                return erreurModele;
            }

            List<ErreurDetaillee> erreurs = validerDonnees(input);
            if (!erreurs.isEmpty()) {
                String messageGlobal = erreurs.stream()
                        .map(e -> "Le champ '" + e.getChamp() + "' : " + e.getMessage())
                        .collect(Collectors.joining(", "));

                historisationService.sauvegarderHistorisation(input, null, null, STATUS_ECHEC, erreurs);

                return new ConventionBinaireRes(false, null, "Les erreurs suivantes ont été détectées : " + messageGlobal);
            }

            Etudiant etudiant = sauvegarderEtudiant(input.getEtudiant());
            MaitreDeStage maitreDeStage = sauvegarderMaitreDeStage(input.getMaitreDeStage());
            if (isTuteurIncomplet(input.getTuteur())) {
                String message = "Le tuteur est manquant ou incomplet.";
                logger.error(message);
                return new ConventionBinaireRes(false, null, message);
            }

            Tuteur tuteur = sauvegarderTuteur(input.getTuteur());
            if (tuteur == null) {
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

            Map<String, String> replacements = prepareReplacements(input, etudiant, maitreDeStage, tuteur);

            byte[] fichierBinaire;
            if (modele.getFichierBinaire() != null) {
                logger.info("Utilisation du modèle en BDD (fichier BLOB)");
                fichierBinaire = docxGenerator.generateDocxFromTemplate(modele.getFichierBinaire(), replacements);
            } else {
                logger.info("Utilisation du modèle depuis les ressources : {}", modele.getNom());
                String path = ResourceUtils.getFile("classpath:conventionServices/" + modele.getNom()).getPath();
                String outputFilePath = docxGenerator.generateDocx(path, replacements, "output/convention_" + System.currentTimeMillis() + ".docx");
                fichierBinaire = Files.readAllBytes(new File(outputFilePath).toPath());
            }

            logger.info("Enregistrement de l'historisation pour la convention générée avec succès.");
            historisationService.sauvegarderHistorisation(input, convention, fichierBinaire, "SUCCES", null);

            logger.info("La convention a été générée avec succès.");
            return new ConventionBinaireRes(true, fichierBinaire, null);

        } catch (ModelNotFoundException e) {
            logger.error("Modèle introuvable : {}", e.getMessage());
            historisationService.sauvegarderHistorisation(
                    input,
                    null,
                    null,
                    STATUS_ECHEC,
                    List.of(new ErreurDetaillee("modele", e.getMessage(), ErreurType.FLUX))
            );
            return new ConventionBinaireRes(false, null, e.getMessage());

        } catch (Exception e) {
            logger.error("Une erreur inattendue s'est produite : {}", e.getMessage(), e);
            historisationService.sauvegarderHistorisation(
                    input,
                    null,
                    null,
                    STATUS_ECHEC,
                    List.of(new ErreurDetaillee("technique", e.getMessage(), ErreurType.TECHNIQUE))
            );
            return new ConventionBinaireRes(false, null, "Erreur inattendue : contacter l’administrateur.");
        }
    }

    @Override
    public List<ErreurDetaillee> validerDonnees(ConventionServiceDTO input) {
        logger.info("Début de la validation des données...");
        ValidationContext context = new ValidationContext();
        context.addStrategy(new EtudiantValidationStrategy());
        context.addStrategy(new MaitreDeStageValidationStrategy());
        context.addStrategy(new TuteurValidationStrategy());
        context.addStrategy(new OrganismeValidationStrategy());
        context.addStrategy(new StageValidationStrategy());

        Map<String, String> erreursBrutes = context.executeValidations(input);
        List<ErreurDetaillee> erreurs = new ArrayList<>();

        for (Map.Entry<String, String> entry : erreursBrutes.entrySet()) {
            erreurs.add(new ErreurDetaillee(entry.getKey(), entry.getValue(), ErreurType.JSON));
        }

        if (input.getModeleId() == null) {
            erreurs.add(new ErreurDetaillee("modeleId", ErrorMessages.MISSING_MODEL_ID, ErreurType.FLUX));
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

    public Tuteur sauvegarderTuteur(TuteurDTO tuteurDTO) {
        logger.info("Début de la sauvegarde de tuteur : {}", tuteurDTO.getNom());
        Tuteur tuteur = TuteurFactory.createTuteur(tuteurDTO);
        Tuteur savedTuteur = tuteurRepository.save(tuteur);
        if (savedTuteur.getId() == null) {
            logger.error("Erreur : l'ID du tuteur sauvegardé est null.");
            return null;
        }
        logger.info("Tuteur sauvegardé avec succès : {}", savedTuteur.getNom());
        return savedTuteur;
    }

    @Transactional(readOnly = true)
    public List<Modele> getModelesByAnnee(String annee) {
        List<Modele> modeles = modeleRepository.findByAnnee(annee);

        if (modeles.isEmpty()) {
            throw new ModelNotFoundException("Aucun modèle trouvé pour l'année : " + annee);
        }
        return modeles;
    }

    public Map<String, String> prepareReplacements(ConventionServiceDTO input, Etudiant etudiant, MaitreDeStage maitreDeStage, Tuteur tuteur) {
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