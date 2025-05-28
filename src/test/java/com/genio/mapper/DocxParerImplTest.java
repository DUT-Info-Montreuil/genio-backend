package com.genio.mapper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocxParerImplTest {

    @Test
    void testExtractVariablesFromDocxInMemory() throws Exception {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.createRun().setText("Bonjour ${prenom} ${nom}, votre stage commence le ${dateDebut}.");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "fake.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                baos.toByteArray()
        );

        DocxParserImpl parser = new DocxParserImpl();
        List<String> variables = parser.extractVariables(mockFile);

        assertEquals(3, variables.size());
        assertTrue(variables.contains("prenom"));
        assertTrue(variables.contains("nom"));
        assertTrue(variables.contains("dateDebut"));
    }
}