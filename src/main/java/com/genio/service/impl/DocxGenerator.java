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
        File outputFile = new File(outputPath);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (InputStream fis = new FileInputStream(conventionServicePath);
             XWPFDocument document = new XWPFDocument(fis)) {

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

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
            }

            logger.info("Fichier DOCX généré avec succès : {}", outputPath);
            return outputPath;

        } catch (IOException e) {
            logger.error("Erreur lors de la génération du DOCX : {}", e.getMessage(), e);
            throw new DocxGenerationException("Erreur lors de la génération du fichier DOCX", e);
        }
    }

    public byte[] generateDocxFromTemplate(byte[] templateBytes, Map<String, String> replacements) throws DocxGenerationException {
        try (InputStream is = new ByteArrayInputStream(templateBytes);
             XWPFDocument document = new XWPFDocument(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

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

            document.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            logger.error("Erreur lors de la génération du DOCX à partir du template BLOB : {}", e.getMessage(), e);
            throw new DocxGenerationException("Erreur lors de la génération du fichier DOCX à partir du template", e);
        }
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
            throw new UnreplacedPlaceholderException("Certains placeholders n'ont pas été remplacés : " + updatedText);
        }

        if (!paragraph.getRuns().isEmpty()) {
            paragraph.getRuns().get(0).setText(updatedText, 0);
        } else {
            paragraph.createRun().setText(updatedText);
        }
    }
}