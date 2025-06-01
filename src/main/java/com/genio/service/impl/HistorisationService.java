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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genio.config.ErreurDetaillee;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.Convention;
import com.genio.model.ErrorDetails;
import com.genio.model.Historisation;
import com.genio.repository.ErrorDetailsRepository;
import com.genio.repository.HistorisationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class HistorisationService {

    private static final Logger logger = LoggerFactory.getLogger(HistorisationService.class);

    private final HistorisationRepository historisationRepository;
    private final ErrorDetailsRepository errorDetailsRepository;

    public HistorisationService(HistorisationRepository historisationRepository,
                                ErrorDetailsRepository errorDetailsRepository) {
        this.historisationRepository = historisationRepository;
        this.errorDetailsRepository = errorDetailsRepository;
    }

    @Transactional
    public void sauvegarderHistorisation(ConventionServiceDTO input, Convention convention, byte[] fichierBinaire, String status, List<ErreurDetaillee> erreurs) {
        try {
            logger.info("Début de la sauvegarde de l'historisation. Statut = {}", status);

            Historisation historisation = new Historisation();
            historisation.setConvention(convention);
            historisation.setStatus(status);
            historisation.setFluxJsonBinaire(new ObjectMapper().writeValueAsBytes(input));
            historisation.setTimestamp();
            logger.debug("Historisation initialisée avec le statut et le flux JSON.");

            if (fichierBinaire != null) {
                historisation.setDocxBinaire(fichierBinaire);
                logger.debug("Fichier binaire associé à l'historisation.");
            }

            if (erreurs != null && !erreurs.isEmpty()) {
                logger.info("Erreurs détectées : {} erreur(s) à historiser.", erreurs.size());
                String detailsConcat = erreurs.stream()
                        .map(ErreurDetaillee::getMessage)
                        .reduce((a, b) -> a + " ; " + b)
                        .orElse("Erreur inconnue");

                historisation.setDetails(detailsConcat);
                historisationRepository.save(historisation);
                logger.debug("Historisation enregistrée avec les erreurs concaténées.");

                for (ErreurDetaillee err : erreurs) {
                    ErrorDetails errorDetails = new ErrorDetails();
                    errorDetails.setHistorisation(historisation);
                    errorDetails.setChampsManquants(err.getChamp());
                    errorDetails.setMessageErreur(err.getMessage());
                    errorDetailsRepository.save(errorDetails);
                    logger.debug("Erreur enregistrée : champ='{}', message='{}'", err.getChamp(), err.getMessage());
                }
            } else {
                historisation.setDetails("Aucune erreur détectée.");
                historisationRepository.save(historisation);
                logger.info("Historisation enregistrée sans erreurs.");
            }

        } catch (Exception e) {
            logger.error("Erreur lors de l'historisation : {}", e.getMessage(), e);
        }
    }
}