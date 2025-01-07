package com.genio;

import com.genio.service.impl.ModeleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenioServiceApplication implements CommandLineRunner {

    @Autowired
    private ModeleService modeleService;

    public static void main(String[] args) {
        SpringApplication.run(GenioServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Appel de la méthode pour insérer les modèles au démarrage
        modeleService.insertModeleFromDirectory();
    }
}