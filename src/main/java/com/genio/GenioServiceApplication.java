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

package com.genio;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.URL;

@SpringBootApplication
public class GenioServiceApplication {

    private static final Logger logger = LogManager.getLogger(GenioServiceApplication.class);

    public static void initializeLogging() {
        URL log4jConfigFile = GenioServiceApplication.class.getClassLoader().getResource("log4j2.xml");
        if (log4jConfigFile == null) {
            logger.error("Fichier log4j2.xml introuvable !");
        } else {
            logger.info("log4j2.xml trouvé à : {}", log4jConfigFile);
            Configurator.initialize(null, log4jConfigFile.getPath());
        }

        logger.info("Application démarrée avec Log4J2 !");
    }

    public static void main(String[] args) {
        initializeLogging();
        SpringApplication.run(GenioServiceApplication.class, args);
    }
}