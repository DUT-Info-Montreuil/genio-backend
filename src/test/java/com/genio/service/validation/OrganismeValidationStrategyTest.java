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

package com.genio.service.validation;

import com.genio.config.ErrorMessages;
import com.genio.dto.OrganismeDTO;
import com.genio.dto.input.ConventionServiceDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrganismeValidationStrategyTest {

    private final OrganismeValidationStrategy strategy = new OrganismeValidationStrategy();

    @Test
    void testValidate_shouldReturnErrorWhenOrganismeIsNull() {
        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setOrganisme(null);

        Map<String, String> errors = strategy.validate(dto);

        assertEquals(1, errors.size());
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_NAME, errors.get("organisme"));
    }

    @Test
    void testValidate_shouldReturnAllFieldErrors() {
        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("")  
                .adresse("") 
                .nomRepresentant("")  
                .qualiteRepresentant("")  
                .nomDuService("")  
                .telephone("123456") 
                .email("invalid-email") 
                .lieuDuStage("")  
                .build();

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setOrganisme(organisme);

        Map<String, String> errors = strategy.validate(dto);

        assertEquals(8, errors.size());
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_NAME, errors.get("organisme.nom"));
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_ADDRESS, errors.get("organisme.adresse"));
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_NAME, errors.get("organisme.nomRepresentant"));
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_REPRESENTATIVE_QUALITY, errors.get("organisme.qualiteRepresentant"));
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_SERVICE_NAME, errors.get("organisme.nomDuService"));
        assertEquals(ErrorMessages.INVALID_ORGANIZATION_PHONE, errors.get("organisme.telephone"));
        assertEquals(ErrorMessages.INVALID_ORGANIZATION_EMAIL, errors.get("organisme.email"));
        assertEquals(ErrorMessages.MISSING_ORGANIZATION_STAGE_LOCATION, errors.get("organisme.lieuDuStage"));
    }

    @Test
    void testValidate_shouldPassWithValidOrganisme() {
        OrganismeDTO organisme = OrganismeDTO.builder()
                .nom("Entreprise X")
                .adresse("123 rue de Paris")
                .nomRepresentant("Jean Dupont")
                .qualiteRepresentant("Directeur")
                .nomDuService("Informatique")
                .telephone("01.23.45.67.89")
                .email("test@entreprise.com")
                .lieuDuStage("Paris")
                .build();

        ConventionServiceDTO dto = new ConventionServiceDTO();
        dto.setOrganisme(organisme);

        Map<String, String> errors = strategy.validate(dto);
        assertTrue(errors.isEmpty());
    }
}
