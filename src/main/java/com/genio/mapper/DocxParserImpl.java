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
 *  https://github.com/DUT-Info-Montreuil/genio-backend
 */

package com.genio.mapper;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.*;

@Component
public class DocxParserImpl implements DocxParser {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*?)}");

    private String normalize(String variable) {
        return Normalizer.normalize(variable, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    @Override
    public List<String> extractVariables(MultipartFile file) throws IOException {
        List<String> vars = new ArrayList<>();

        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            for (XWPFParagraph p : doc.getParagraphs()) {
                extractFromParagraph(p, vars);
            }

            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            extractFromParagraph(p, vars);
                        }
                    }
                }
            }
        }

        return vars;
    }

    private void extractFromParagraph(XWPFParagraph paragraph, List<String> vars) {
        StringBuilder text = new StringBuilder();
        for (XWPFRun run : paragraph.getRuns()) {
            String part = run.getText(0);
            if (part != null) text.append(part);
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(text.toString());
        while (matcher.find()) {
            vars.add(normalize(matcher.group(1)));
        }
    }
}