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
            Historisation historisation = new Historisation();
            historisation.setConvention(convention);
            historisation.setStatus(status);
            historisation.setFluxJsonBinaire(new ObjectMapper().writeValueAsBytes(input));
            historisation.setTimestamp();

            if (fichierBinaire != null) {
                historisation.setDocxBinaire(fichierBinaire);
            }

            if (erreurs != null && !erreurs.isEmpty()) {
                String detailsConcat = erreurs.stream()
                        .map(ErreurDetaillee::getMessage)
                        .reduce((a, b) -> a + " ; " + b)
                        .orElse("Erreur inconnue");

                historisation.setDetails(detailsConcat);
                historisationRepository.save(historisation);

                for (ErreurDetaillee err : erreurs) {
                    ErrorDetails errorDetails = new ErrorDetails();
                    errorDetails.setHistorisation(historisation);
                    errorDetails.setChampsManquants(err.getChamp());
                    errorDetails.setMessageErreur(err.getMessage());
                    errorDetailsRepository.save(errorDetails);
                }
            } else {
                historisation.setDetails("Aucune erreur détectée.");
                historisationRepository.save(historisation);
            }

        } catch (Exception e) {
            logger.error("Erreur lors de l'historisation : {}", e.getMessage());
        }
    }

}