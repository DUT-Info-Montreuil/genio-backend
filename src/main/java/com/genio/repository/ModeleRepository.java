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

import com.genio.model.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeleRepository extends JpaRepository<Modele, Long> {

    @Query(value = "SELECT m FROM Modele m WHERE m.annee = :annee", nativeQuery = false)
    List<Modele> findByAnnee(@Param("annee") String annee);

    @Query(value = "SELECT COUNT(m) FROM Modele m WHERE m.annee = :annee", nativeQuery = false)
    long countByAnnee(@Param("annee") String annee);
    Optional<Modele> findFirstByNom(String nom);
    Optional<Modele> findFirstByFichierHash(String fichierHash);
    Optional<Modele> findFirstByNomAndArchivedFalse(String nom);
    Optional<Modele> findFirstByAnneeAndArchivedFalse(String annee);
}