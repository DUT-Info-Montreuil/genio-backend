package com.genio.service.impl;

import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

public class DocxGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DocxGenerator.class);

    public static String generateDocx(String templatePath, Map<String, String> replacements, String outputPath) throws Exception {
        File outputFile = new File(outputPath);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        logger.info("Début de la génération du fichier DOCX...");
        logger.info("Chemin du modèle : {}", templatePath);
        logger.info("Chemin de sortie : {}", outputPath);
        logger.debug("Placeholders fournis : {}", replacements);

        try (InputStream fis = new FileInputStream(templatePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Remplacer les placeholders dans les paragraphes
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInParagraph(paragraph, replacements);
            }

            // Remplacer les placeholders dans les tableaux
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replacePlaceholdersInParagraph(paragraph, replacements);
                        }
                    }
                }
            }

            // Sauvegarder le fichier généré
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
            }

            logger.info("Fichier DOCX généré avec succès : {}", outputPath);
            return outputPath;

        } catch (Exception e) {
            logger.error("Erreur lors de la génération du fichier DOCX : {}", e.getMessage(), e);
            throw e;
        }
    }

    private static void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        StringBuilder paragraphText = new StringBuilder();

        // Récupérer tout le texte dans les runs du paragraphe
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                paragraphText.append(text);
            }
            run.setText("", 0);  // Effacer le texte du run
        }

        // Remplacer les placeholders
        String updatedText = paragraphText.toString();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            updatedText = updatedText.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Vérifier si des placeholders non remplacés subsistent
        if (updatedText.contains("${")) {
            logger.warn("Placeholders non remplacés détectés : {}", updatedText);
            throw new RuntimeException("Certains placeholders n'ont pas été remplacés : " + updatedText);
        }

        // Réécrire le texte mis à jour dans le paragraphe
        if (!paragraph.getRuns().isEmpty()) {
            paragraph.getRuns().get(0).setText(updatedText, 0);
        } else {
            paragraph.createRun().setText(updatedText);
        }
    }
}