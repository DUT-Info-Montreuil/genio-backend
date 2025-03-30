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
                Matcher matcher = pattern.matcher(p.getText());
                while (matcher.find()) {
                    String rawVariable = matcher.group(1);
                    vars.add(normalize(rawVariable));
                }
            }
        }
        return vars;
    }
}