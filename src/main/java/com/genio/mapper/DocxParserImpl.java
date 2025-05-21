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
    private String normalize(String variable) {
        return Normalizer.normalize(variable, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    @Override
    public List<String> extractVariables(MultipartFile file) throws IOException {
        List<String> vars = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");

        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {

            for (XWPFParagraph p : doc.getParagraphs()) {
                StringBuilder text = new StringBuilder();
                for (XWPFRun run : p.getRuns()) {
                    if (run.getText(0) != null) {
                        text.append(run.getText(0));
                    }
                }
                Matcher matcher = pattern.matcher(text.toString());
                while (matcher.find()) {
                    String rawVariable = matcher.group(1);
                    vars.add(normalize(rawVariable));
                }
            }

            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            StringBuilder text = new StringBuilder();
                            for (XWPFRun run : p.getRuns()) {
                                if (run.getText(0) != null) {
                                    text.append(run.getText(0));
                                }
                            }
                            Matcher matcher = pattern.matcher(text.toString());
                            while (matcher.find()) {
                                String rawVariable = matcher.group(1);
                                vars.add(normalize(rawVariable));
                            }
                        }
                    }
                }
            }

        }

        return vars;
    }
}