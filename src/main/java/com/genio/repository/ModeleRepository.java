package com.genio.repository;

import com.genio.model.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeleRepository extends JpaRepository<Modele, Long> {
    Modele findByAnnee(String annee);
}
