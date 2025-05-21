package com.genio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

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
    public void sauvegarderHistorisation(ConventionServiceDTO input, Convention convention, byte[] fichierBinaire, String status, Map<String, String> erreurs) {
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
                String messageErreurConcat = erreurs.values().stream()
                        .reduce((a, b) -> a + " ; " + b)
                        .orElse("Erreur inconnue");

                historisation.setDetails(messageErreurConcat);
            } else {
                historisation.setDetails("Aucune erreur détectée.");
            }

            historisationRepository.save(historisation);

            if (erreurs != null && !erreurs.isEmpty()) {
                String messageErreurBrut = erreurs.toString();
                if (messageErreurBrut.length() > 255) {
                    messageErreurBrut = messageErreurBrut.substring(0, 255);
                }

                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setMessageErreur(messageErreurBrut);
                errorDetails.setHistorisation(historisation);

                StringBuilder champsManquants = new StringBuilder();
                erreurs.forEach((key, value) -> champsManquants.append(key).append(" ; "));
                errorDetails.setChampsManquants(champsManquants.toString());

                errorDetailsRepository.save(errorDetails);
            }

        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde de l'historisation : {}", e.getMessage());
        }
    }


}