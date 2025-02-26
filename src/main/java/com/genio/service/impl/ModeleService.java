package com.genio.service.impl;

import com.genio.dto.output.ModeleDTO;
import com.genio.exception.business.*;
import com.genio.model.Modele;
import com.genio.repository.ModeleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ModeleService {

    private static final Logger logger = LoggerFactory.getLogger(ModeleService.class);

    @Value("${modele.templates.directory}")
    private String directoryPath;

    private final DataSource dataSource;

    @Autowired
    private ModeleRepository modeleRepository;

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

        Pattern pattern = Pattern.compile("_(\\d{4})\\.docx$");
        Matcher matcher = pattern.matcher(modelName);

        if (!matcher.find()) {
            logger.error("Erreur : Le fichier {} ne respecte pas le format attendu (_YYYY.docx).", modelName);
            throw new InvalidFileFormatException("Le fichier " + modelName + " ne respecte pas le format attendu (_YYYY.docx).");
        }

        String modelYear = matcher.group(1);
        logger.info("Année extraite : {}", modelYear);

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

    public List<ModeleDTO> getAllTemplates() throws NoTemplatesAvailableException {
        List<Modele> modeles = modeleRepository.findAll();

        if (modeles.isEmpty()) {
            throw new NoTemplatesAvailableException("Aucun modèle de convention disponible.");
        }

        return modeles.stream()
                .map(modele -> new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx"))
                .collect(Collectors.toList());
    }


    public ModeleDTO getTemplateById(Long id) throws TemplateNotFoundException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException("Modèle introuvable avec l'ID : " + id));
        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx");
    }

    public ModeleDTO createTemplate(ModeleDTO modeleDTO) {
        Modele modele = new Modele();
        modele.setNom(modeleDTO.getNom());
        modele.setAnnee(modeleDTO.getAnnee());
        modeleRepository.save(modele);

        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx");
    }
    public ModeleDTO updateTemplate(Long id, ModeleDTO modeleDTO) throws NoTemplatesAvailableException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new NoTemplatesAvailableException("Modèle introuvable avec l'ID : " + id));

        modele.setNom(modeleDTO.getNom());
        modele.setAnnee(modeleDTO.getAnnee());
        modeleRepository.save(modele);

        return new ModeleDTO(modele.getId(), modele.getNom(), modele.getAnnee(), "docx");
    }

    public void deleteTemplate(Long id) throws NoTemplatesAvailableException {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new NoTemplatesAvailableException("Modèle introuvable avec l'ID : " + id));

        modeleRepository.delete(modele);
    }

}