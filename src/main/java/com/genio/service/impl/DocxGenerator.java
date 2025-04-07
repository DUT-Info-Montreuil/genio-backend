package com.genio.service.impl;

import com.genio.exception.business.DocxGenerationException;
import com.genio.exception.business.UnreplacedPlaceholderException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class DocxGenerator {

    public String generateDocx(String conventionServicePath, Map<String, String> replacements, String outputPath)
            throws DocxGenerationException {
        File outputFile = new File(outputPath);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (InputStream fis = new FileInputStream(conventionServicePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            processDocument(document, replacements);

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
            }

            return outputPath;

        } catch (FileNotFoundException e) {
            throw new DocxGenerationException("Fichier source non trouvé : " + conventionServicePath, e);
        } catch (IOException e) {
            throw new DocxGenerationException("Erreur d'E/S lors de la génération du fichier DOCX depuis : "
                    + conventionServicePath + " vers " + outputPath, e);
        }
    }

    public byte[] generateDocxFromTemplate(byte[] templateBytes, Map<String, String> replacements)
            throws DocxGenerationException {
        try (InputStream is = new ByteArrayInputStream(templateBytes);
             XWPFDocument document = new XWPFDocument(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            processDocument(document, replacements);
            document.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new DocxGenerationException("Erreur lors de la génération du fichier DOCX à partir du template binaire", e);
        }
    }

    private void processDocument(XWPFDocument document, Map<String, String> replacements) {
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
            throw new UnreplacedPlaceholderException("Certains placeholders n'ont pas été remplacés : " + missing);
        }

        if (!paragraph.getRuns().isEmpty()) {
            paragraph.getRuns().get(0).setText(updatedText, 0);
        } else {
            paragraph.createRun().setText(updatedText);
        }
    }
}