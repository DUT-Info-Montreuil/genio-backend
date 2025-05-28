package com.genio.service.impl;

import com.genio.config.ErreurDetaillee;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.model.Convention;
import com.genio.model.ErrorDetails;
import com.genio.model.Historisation;
import com.genio.repository.ErrorDetailsRepository;
import com.genio.repository.HistorisationRepository;
import com.genio.utils.ErreurType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class HistorisationServiceTest {

    @Mock
    private HistorisationRepository historisationRepository;

    @Mock
    private ErrorDetailsRepository errorDetailsRepository;

    @InjectMocks
    private HistorisationService historisationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSauvegarderHistorisation_withErrors_shouldSaveHistorisationAndErrorDetails() {
        ConventionServiceDTO input = new ConventionServiceDTO();
        Convention convention = new Convention();
        byte[] fichier = new byte[]{1, 2, 3};

        List<ErreurDetaillee> erreurs = List.of(
                new ErreurDetaillee("champ1", "erreur1", ErreurType.JSON)
        );

        historisationService.sauvegarderHistorisation(input, convention, fichier, "ECHEC", erreurs);

        verify(historisationRepository, times(1)).save(any(Historisation.class));
        verify(errorDetailsRepository, times(1)).save(any(ErrorDetails.class));
    }

    @Test
    void testSauvegarderHistorisation_withoutErrors_shouldSaveHistorisationOnly() {
        ConventionServiceDTO input = new ConventionServiceDTO();
        Convention convention = new Convention();
        byte[] fichier = new byte[]{4, 5, 6};

        historisationService.sauvegarderHistorisation(input, convention, fichier, "SUCCES", null);

        verify(historisationRepository, times(1)).save(any(Historisation.class));
        verify(errorDetailsRepository, never()).save(any(ErrorDetails.class));
    }
}