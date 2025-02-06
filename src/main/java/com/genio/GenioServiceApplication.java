package com.genio;


import com.genio.service.impl.ModeleService;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.URL;

@SpringBootApplication
public class GenioServiceApplication implements CommandLineRunner {


    private ModeleService modeleService;

    public GenioServiceApplication(ModeleService modeleService) {
        this.modeleService = modeleService;
    }

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

    @Override
    public void run(String... args) throws Exception {
        modeleService.insertModeleFromDirectory();
    }


}