package com.example.controller;

import com.example.model.ResumeRequest;
import com.example.model.ApiResponse;
import com.example.service.ResumeAnalysisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(
    origins = {
        "https://ats-resume-calculator-frontend.onrender.com",
        "http://localhost:3000"
    },
    allowedHeaders = "*",
    allowCredentials = "true",
    methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.PATCH
    }
)
public class ResumeController {
    
    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
    
    @Autowired
    private ResumeAnalysisService resumeAnalysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(
            @Valid @ModelAttribute ResumeRequest request,
            HttpServletRequest httpRequest) {
        
        String fileName = request.getResumeFile() != null ? 
            request.getResumeFile().getOriginalFilename() : "unknown";
        
        log.info("Received resume analysis request - File: {}, Provider: {}", 
            fileName, request.getModelProvider());
        
        try {
            // Your existing code...
            Map<String, Object> result = resumeAnalysisService.analyzeResume(request, httpRequest);
            return ResponseEntity.ok(ApiResponse.success("Analysis completed successfully", result));
        } catch (Exception e) {
            log.error("Error analyzing resume: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Server Error", e.getMessage()));
        }
    }
    
    @PostMapping("/analyze-all")
    public ResponseEntity<?> analyzeWithAllModels(
            @Valid @ModelAttribute ResumeRequest request,
            HttpServletRequest httpRequest) {
        
        String fileName = request.getResumeFile() != null ? 
            request.getResumeFile().getOriginalFilename() : "unknown";
        
        log.info("Received multi-model analysis request - File: {}", fileName);
        
        try {
            Map<String, Object> result = resumeAnalysisService.analyzeWithAllModels(request, httpRequest);
            return ResponseEntity.ok(ApiResponse.success("Multi-model analysis completed", result));
        } catch (Exception e) {
            log.error("Error analyzing with all models: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Server Error", e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "ATS Resume Calculator");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/supported-formats")
    public ResponseEntity<?> getSupportedFormats() {
        Map<String, Object> formats = new HashMap<>();
        formats.put("supportedFormats", new String[]{"PDF", "DOCX"});
        formats.put("maxFileSize", "10MB");
        formats.put("message", "Upload resumes in PDF or DOCX format");
        return ResponseEntity.ok(formats);
    }
}
