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

package com.genio.repository;

import com.genio.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConventionRepository extends JpaRepository<Convention, Long> {
    long countByModele_Id(Long modeleId);
}