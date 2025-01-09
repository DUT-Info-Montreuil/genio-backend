package com.genio;

import com.genio.controller.GenioController;
import com.genio.service.impl.ModeleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenioServiceApplication implements CommandLineRunner {

    @Autowired
    private ModeleService modeleService;

    private static final Logger logger = LoggerFactory.getLogger(GenioServiceApplication.class);

    public static void main(String[] args) {
        logger.info("L'application démarre...");
        SpringApplication.run(GenioServiceApplication.class, args);
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
    }

    @Override
    public void run(String... args) throws Exception {
        // Appel de la méthode pour insérer les modèles au démarrage
        modeleService.insertModeleFromDirectory();
    }

    public void someMethod() {
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
    }


}