package com.example.utils;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileParserUtil {
    
    private static final Logger log = LoggerFactory.getLogger(FileParserUtil.class);
    private final Tika tika;
    
    public FileParserUtil() {
        this.tika = new Tika();
    }
    
    public String extractText(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        log.info("Extracting text from file: {}, extension: {}", fileName, extension);
        
        // Validate file type
        if (!extension.matches("pdf|docx|doc|txt|rtf")) {
            throw new IllegalArgumentException("Unsupported file format. Please upload PDF, DOCX, DOC, TXT, or RTF.");
        }
        
        try {
            String text = tika.parseToString(file.getInputStream());
            
            if (text == null || text.trim().isEmpty()) {
                throw new Exception("No text could be extracted from the file");
            }
            
            // Clean up extra whitespace
            text = text.replaceAll("\\s+", " ").trim();
            
            log.info("Successfully extracted {} characters from file: {}", text.length(), fileName);
            return text;
            
        } catch (IOException | TikaException e) {
            log.error("Error extracting text from file: {}", fileName, e);
            throw new Exception("Failed to extract text from file: " + e.getMessage(), e);
        }
    }
}