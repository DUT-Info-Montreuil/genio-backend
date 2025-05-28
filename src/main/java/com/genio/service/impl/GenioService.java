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

import com.genio.config.ErreurDetaillee;
import com.genio.dto.input.ConventionServiceDTO;
import com.genio.dto.outputmodeles.ConventionBinaireRes;
import com.genio.exception.business.GenerationConventionException;
import com.genio.exception.business.InvalidDataException;
import com.genio.exception.business.ModelNotFoundException;
import com.genio.exception.technical.FileConversionException;
import com.genio.exception.technical.SystemErrorException;
import com.genio.model.Modele;

import java.util.List;


public interface GenioService {

    /**
     * Génère une convention à partir des données d'entrée.
     *
     * @param input Les données d'entrée sous forme de DTO.
     * @param formatFichierOutput Le format souhaité (PDF, DOCX).
     * @return Le résultat contenant le fichier binaire ou un code de réponse.
     * @throws IllegalArgumentException Si les données d'entrée sont invalides.
     * @throws ModelNotFoundException Si le modèle est introuvable.
     * @throws InvalidDataException Si les données de l'utilisateur sont invalides.
     * @throws GenerationConventionException Si la génération du document échoue.
     * @throws FileConversionException Si la conversion du fichier échoue.
     * @throws SystemErrorException Si une erreur système survient pendant le traitement.
     */
    ConventionBinaireRes generateConvention(ConventionServiceDTO input, String formatFichierOutput)
            throws IllegalArgumentException, ModelNotFoundException, InvalidDataException,
            GenerationConventionException, FileConversionException, SystemErrorException;

    List<ErreurDetaillee> validerDonnees(ConventionServiceDTO input);
    boolean modeleExiste(Long modeleId);
    List<Modele> getModelesByAnnee(String annee);
}