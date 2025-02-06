package com.genio.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ModeleService {

    @Value("${modele.templates.directory}")
    private String directoryPath;

    private final DataSource dataSource;

    public ModeleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertModeleFromDirectory() throws IOException, SQLException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));

        if (files != null) {
            for (File file : files) {
                insertModele(file);
            }
        }
    }

    private void insertModele(File templateFile) throws IOException, SQLException {
        byte[] fileBytes = Files.readAllBytes(templateFile.toPath());
        String modelName = templateFile.getName();

        System.out.println("Nom du fichier trouvé : " + modelName);

        // Regex pour vérifier le format "_YYYY.docx"
        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);

        if (!matcher.find()) {
            System.err.println("Erreur : Le fichier " + modelName + " ne respecte pas le format attendu (_YYYY.docx).");
            return; // Arrête ici et ne fait pas l'insertion en base
        }

        String modelYear = matcher.group(1); // Extrait l'année (ex: "2024")
        System.out.println("Année extraite : " + modelYear);

        if (dataSource != null) {
            String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, modelName);
                preparedStatement.setString(2, modelYear);
                preparedStatement.setBytes(3, fileBytes);
                preparedStatement.executeUpdate();
                System.out.println("Modèle inséré avec succès : " + modelName);
            }
        } else {
            System.out.println("La base de données n'est pas configurée. Aucun modèle n'a été inséré.");
        }
    }
}