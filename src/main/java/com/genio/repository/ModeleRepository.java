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
}