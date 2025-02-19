package com.genio.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.genio.exception.business.InvalidFileFormatException;
import com.genio.exception.business.DatabaseInsertionException;
import com.genio.exception.business.EmptyDirectoryException;
import com.genio.exception.business.EmptyFileException;

@Service
public class ModeleService {

    private static final Logger logger = LoggerFactory.getLogger(ModeleService.class);

    @Value("${modele.templates.directory}")
    private String directoryPath;

    private final DataSource dataSource;

    public ModeleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertModeleFromDirectory() throws IOException, SQLException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));

        if (files == null || files.length == 0) {
            logger.warn("Le répertoire spécifié ne contient aucun fichier .docx.");
            throw new EmptyDirectoryException("Le répertoire ne contient aucun fichier .docx.");
        } else {
            for (File file : files) {
                insertModele(file);
            }
        }
    }

    public void insertModele(File templateFile) throws IOException, SQLException {
        if (templateFile.length() == 0) {
            throw new EmptyFileException("Le fichier " + templateFile.getName() + " est vide.");
        }

        byte[] fileBytes = Files.readAllBytes(templateFile.toPath());
        String modelName = templateFile.getName();

        logger.info("Nom du fichier trouvé : {}", modelName);
        logger.info("Taille du fichier : {} octets", templateFile.length());
        logger.info("Date de création du fichier : {}", new java.util.Date(templateFile.lastModified()));

        // Validation du format du fichier
        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);

        if (!matcher.find()) {
            logger.error("Erreur : Le fichier {} ne respecte pas le format attendu (_YYYY.docx).", modelName);
            throw new InvalidFileFormatException("Le fichier " + modelName + " ne respecte pas le format attendu (_YYYY.docx).");
        }

        String modelYear = matcher.group(1);
        logger.info("Année extraite : {}", modelYear);

        // Insertion en base de données
        if (dataSource != null) {
            String sql = "INSERT INTO modele (nom, annee, fichier_binaire) VALUES (?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, modelName);
                preparedStatement.setString(2, modelYear);
                preparedStatement.setBytes(3, fileBytes);
                preparedStatement.executeUpdate();
                logger.info("Modèle inséré avec succès : {}", modelName);
            } catch (SQLException e) {
                logger.error("Erreur lors de l'insertion du modèle {} dans la base de données. Détails : {}", modelName, e.getMessage());
                throw new DatabaseInsertionException("Erreur lors de l'insertion du modèle " + modelName + " dans la base de données.", e);
            }
        } else {
            logger.warn("La base de données n'est pas configurée. Aucun modèle n'a été inséré.");
        }
    }
}