package com.genio.mapper;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface DocxParser {
    List<String> extractVariables(MultipartFile file) throws IOException;
}