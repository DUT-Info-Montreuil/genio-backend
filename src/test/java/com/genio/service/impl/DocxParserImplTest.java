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

import com.genio.mapper.DocxParserImpl;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocxParserImplTest {

    private final DocxParserImpl parser = new DocxParserImpl();

    @Test
    void testExtractVariables_shouldReturnPlaceholders() throws Exception {
        XWPFDocument doc = new XWPFDocument();
        doc.createParagraph().createRun().setText("Bonjour ${NOM}, voici ton stage à ${ORGANISME}");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                out.toByteArray()
        );

        List<String> variables = parser.extractVariables(file);

        assertTrue(variables.contains("NOM"));
        assertTrue(variables.contains("ORGANISME"));
    }
}