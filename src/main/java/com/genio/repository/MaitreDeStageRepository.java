package com.genio.repository;

import com.genio.model.MaitreDeStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaitreDeStageRepository extends JpaRepository<MaitreDeStage, Long> {
}