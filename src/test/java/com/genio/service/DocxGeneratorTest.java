package com.genio.service;

import com.genio.exception.business.UnreplacedPlaceholderException;
import com.genio.service.impl.DocxGenerator;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DocxGeneratorTest {

    private DocxGenerator docxGenerator;

    @BeforeEach
    void setUp() {
        docxGenerator = new DocxGenerator();
    }

    @Test
    void testGenerateDocxFromTemplate_shouldReplaceVariables() throws Exception {
        XWPFDocument document = new XWPFDocument();
        document.createParagraph().createRun().setText("Bonjour ${NOM}");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        byte[] template = out.toByteArray();

        Map<String, String> replacements = new HashMap<>();
        replacements.put("NOM", "Elsa");

        byte[] resultBytes = docxGenerator.generateDocxFromTemplate(template, replacements);

        XWPFDocument resultDoc = new XWPFDocument(new ByteArrayInputStream(resultBytes));
        String text = resultDoc.getParagraphs().get(0).getText();
        assertEquals("Bonjour Elsa", text);
    }

    @Test
    void testGenerateDocxFromTemplate_shouldThrowException_whenInputIsInvalid() {
        byte[] invalidTemplate = "not a docx".getBytes();
        Map<String, String> replacements = Map.of("NOM", "Elsa");

        Exception exception = assertThrows(
                com.genio.exception.business.DocxGenerationException.class,
                () -> docxGenerator.generateDocxFromTemplate(invalidTemplate, replacements)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("Erreur lors de la génération du fichier DOCX"));
    }

    @Test
    void testGenerateDocx_shouldReplaceVariablesAndWriteFile() throws Exception {
        File templateFile = File.createTempFile("template_", ".docx");
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(templateFile)) {
            doc.createParagraph().createRun().setText("Bonjour ${PRENOM}");
            doc.write(out);
        }

        File outputFile = File.createTempFile("output_", ".docx");
        outputFile.delete();

        Map<String, String> replacements = new HashMap<>();
        replacements.put("PRENOM", "Elsa");

        String resultPath = docxGenerator.generateDocx(
                templateFile.getAbsolutePath(),
                replacements,
                outputFile.getAbsolutePath()
        );

        assertEquals(outputFile.getAbsolutePath(), resultPath);
        assertTrue(new File(resultPath).exists());

        try (XWPFDocument resultDoc = new XWPFDocument(new FileInputStream(resultPath))) {
            String text = resultDoc.getParagraphs().get(0).getText();
            assertEquals("Bonjour Elsa", text);
        }

        templateFile.delete();
        outputFile.delete();
    }

    @Test
    void testProcessDocument_ShouldReplacePlaceholdersInDocument() throws Exception {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("NOM", "Elsa");

        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.createRun().setText("Bonjour ${NOM}");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        byte[] docBytes = out.toByteArray();

        byte[] resultBytes = docxGenerator.generateDocxFromTemplate(docBytes, replacements);

        XWPFDocument resultDoc = new XWPFDocument(new ByteArrayInputStream(resultBytes));
        String resultText = resultDoc.getParagraphs().get(0).getText();
        assertEquals("Bonjour Elsa", resultText);
    }


    @Test
    void testReplacePlaceholdersInParagraph_ShouldThrowException_WhenPlaceholderNotReplaced() {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("NOM", "Elsa");

        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.createRun().setText("Bonjour ${NOM}, ${AGE}");

        Exception exception = assertThrows(UnreplacedPlaceholderException.class, () -> {
            docxGenerator.processDocument(doc, replacements);
        });

        assertTrue(exception.getMessage().contains("${AGE}"));
    }
}