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

    public static void main(String[] args) {
        URL log4jConfigFile = GenioServiceApplication.class.getClassLoader().getResource("log4j2.xml");
        if (log4jConfigFile == null) {
            logger.error("Fichier log4j2.xml introuvable !");
        } else {
            logger.info("log4j2.xml trouvé à : {}", log4jConfigFile);
            Configurator.initialize(null, log4jConfigFile.getPath());
        }

        logger.info("Application démarrée avec Log4J2 !");
        SpringApplication.run(GenioServiceApplication.class, args);
    }
}