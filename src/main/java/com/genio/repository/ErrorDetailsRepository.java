package com.genio.repository;

import com.genio.model.ErrorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorDetailsRepository extends JpaRepository<ErrorDetails, Long> {
    List<ErrorDetails> findAll();
}