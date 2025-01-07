package com.genio.repository;

import com.genio.model.ErrorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorDetailsRepository extends JpaRepository<ErrorDetails, Long> {
}