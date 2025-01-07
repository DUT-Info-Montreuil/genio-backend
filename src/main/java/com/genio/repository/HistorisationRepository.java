package com.genio.repository;

import com.genio.model.Historisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorisationRepository extends JpaRepository<Historisation, Long> {
}