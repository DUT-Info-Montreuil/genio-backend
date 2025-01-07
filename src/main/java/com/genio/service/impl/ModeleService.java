package com.genio.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class ModeleService {

    @Value("${modele.templates.directory}")
    private String directoryPath;

    private final DataSource dataSource;

    public ModeleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Méthode pour insérer des modèles dans la base de données à partir d'un répertoire
    public void insertModeleFromDirectory() throws SQLException, IOException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));  // Filtrer les fichiers .docx

        if (files != null) {
            for (File file : files) {
                insertModele(file);
            }
        }
    }

    // Méthode pour insérer un modèle spécifique
    private void insertModele(File templateFile) throws SQLException, IOException {
        byte[] fileBytes = Files.readAllBytes(templateFile.toPath());

        String modelName = templateFile.getName();
        String modelYear = modelName.substring(modelName.indexOf("_") + 1, modelName.indexOf("."));
        String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, modelName);  // Nom du modèle
            preparedStatement.setString(2, modelYear);  // Année extraite du nom du modèle
            preparedStatement.setBytes(3, fileBytes);  // Fichier binaire du modèle

            preparedStatement.executeUpdate();
        }
    }
}