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

package com.genio.service.impl;

import com.genio.exception.business.DocxGenerationException;
import com.genio.exception.business.UnreplacedPlaceholderException;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class DocxGenerator {


    private static final Logger logger = LoggerFactory.getLogger(DocxGenerator.class);
    public String generateDocx(String conventionServicePath, Map<String, String> replacements, String outputPath)
            throws DocxGenerationException {
        logger.info("Début de la génération du fichier DOCX depuis le modèle : {}", conventionServicePath);
        File outputFile = new File(outputPath);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
            logger.debug("Répertoire de sortie créé : {}", outputDir.getAbsolutePath());
        }

        try (InputStream fis = new FileInputStream(conventionServicePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            logger.debug("Fichier modèle chargé avec succès.");

            processDocument(document, replacements);

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
                logger.info("Fichier DOCX généré avec succès à l'emplacement : {}", outputPath);
            }

            return outputPath;

        } catch (UnreplacedPlaceholderException e) {
            throw new DocxGenerationException("Placeholder non remplacé dans " + conventionServicePath, e);
        } catch (FileNotFoundException e) {
            throw new DocxGenerationException("Fichier modèle introuvable : " + conventionServicePath, e);
        } catch (IOException e) {
            throw new DocxGenerationException("Erreur d'E/S entre le modèle '" + conventionServicePath + "' et le fichier de sortie '" + outputPath + "'", e);
        }
    }

    public byte[] generateDocxFromTemplate(byte[] templateBytes, Map<String, String> replacements)
            throws DocxGenerationException {
        logger.info("Début de la génération du fichier DOCX depuis un modèle binaire...");

        try (InputStream is = new ByteArrayInputStream(templateBytes);
             XWPFDocument document = new XWPFDocument(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            processDocument(document, replacements);
            document.write(out);

            logger.info("Fichier DOCX généré avec succès depuis modèle binaire.");
            return out.toByteArray();
        } catch (UnreplacedPlaceholderException e) {
            throw new DocxGenerationException("Échec lors du remplacement des variables dans le modèle DOCX binaire", e);
        } catch (IOException e) {
            throw new DocxGenerationException("Erreur d'E/S : lecture ou écriture du modèle binaire échouée (longueur = "
                    + (templateBytes != null ? templateBytes.length : "null") + " octets)", e);
        } catch (Exception e) {
            throw new DocxGenerationException("Erreur imprévue durant la génération depuis modèle DOCX binaire (longueur = "
                    + (templateBytes != null ? templateBytes.length : "null") + ")", e);
        }
    }

    public void processDocument(XWPFDocument document, Map<String, String> replacements) {
        logger.debug("Début du traitement du document : remplacement des variables...");

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replacePlaceholdersInParagraph(paragraph, replacements);
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replacePlaceholdersInParagraph(paragraph, replacements);
                    }
                }
            }
        }

        logger.debug("Traitement du document terminé.");
    }

    private void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        StringBuilder paragraphText = new StringBuilder();

        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                paragraphText.append(text);
            }
            run.setText("", 0);
        }

        String updatedText = paragraphText.toString();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            updatedText = updatedText.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        if (updatedText.contains("${")) {
            String missing = updatedText.substring(updatedText.indexOf("${"));
            logger.error("Placeholders non remplacés détectés : {}", missing);
            throw new UnreplacedPlaceholderException("Placeholders non remplacés dans un paragraphe : " + missing);
        }

        try {
            if (!paragraph.getRuns().isEmpty()) {
                paragraph.getRuns().get(0).setText(updatedText, 0);
            } else {
                paragraph.createRun().setText(updatedText);
            }
            logger.trace("Paragraphe traité : {}", updatedText);

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour d’un paragraphe avec le texte modifié '{}'. Replacements fournis : {}",
                    updatedText, replacements.keySet(), e);
            throw new DocxGenerationException("Impossible d’écrire le texte modifié dans un paragraphe : " + updatedText, e);
        }
    }
}