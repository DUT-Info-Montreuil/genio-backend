package com.genio.service.impl;

import com.genio.dto.output.ErreurDTO;
import com.genio.exception.business.InvalidFilterException;
import com.genio.exception.business.NoErrorFoundException;
import com.genio.repository.ErrorDetailsRepository;
import com.genio.model.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;@Service
public class ErreurService {

    @Autowired
    private ErrorDetailsRepository errorDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(ErreurService.class);

    public List<ErreurDTO> getRecentErrors() throws NoErrorFoundException {
        List<ErrorDetails> errorDetails = errorDetailsRepository.findAll();

        if (errorDetails.isEmpty()) {
            logger.error("Aucune erreur trouvée");
            throw new NoErrorFoundException("Aucune erreur trouvée");
        }

        return errorDetails.stream()
                .map(error -> new ErreurDTO(error.getMessageErreur()))
                .collect(Collectors.toList());
    }
}