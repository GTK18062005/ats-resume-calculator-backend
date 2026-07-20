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
public class ResumeController {
    
    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
    
    @Autowired
    private ResumeAnalysisService resumeAnalysisService;
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        log.info("Health check request received");
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "ATS Resume Calculator");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/supported-formats")
    public ResponseEntity<?> getSupportedFormats() {
        log.info("Supported formats request received");
        Map<String, Object> formats = new HashMap<>();
        formats.put("supportedFormats", new String[]{"PDF", "DOCX"});
        formats.put("maxFileSize", "10MB");
        formats.put("message", "Upload resumes in PDF or DOCX format");
        return ResponseEntity.ok(formats);
    }
    
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(
            @Valid @ModelAttribute ResumeRequest request,
            HttpServletRequest httpRequest) {
        
        String fileName = request.getResumeFile() != null ? 
            request.getResumeFile().getOriginalFilename() : "unknown";
        
        log.info("Received resume analysis request - File: {}, Provider: {}", 
            fileName, request.getModelProvider());
        
        try {
            if (request.getResumeFile() == null || request.getResumeFile().isEmpty()) {
                log.warn("Resume file is missing in request");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Resume file is required"));
            }
            
            if (request.getJobDescription() == null || request.getJobDescription().trim().isEmpty()) {
                log.warn("Job description is missing in request");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Job description is required"));
            }
            
            if (fileName != null && !fileName.matches(".*\\.(pdf|docx)$")) {
                log.warn("Unsupported file type: {}", fileName);
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Only PDF and DOCX files are supported"));
            }
            
            Map<String, Object> result = resumeAnalysisService.analyzeResume(request, httpRequest);
            log.info("Analysis completed successfully for file: {}", fileName);
            
            return ResponseEntity.ok(ApiResponse.success("Analysis completed successfully", result));
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request - File: {}, Error: {}", fileName, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid Request", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error analyzing resume - File: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Server Error", "Error analyzing resume: " + e.getMessage()));
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
            if (request.getResumeFile() == null || request.getResumeFile().isEmpty()) {
                log.warn("Resume file is missing in request");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Resume file is required"));
            }
            
            if (request.getJobDescription() == null || request.getJobDescription().trim().isEmpty()) {
                log.warn("Job description is missing in request");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Job description is required"));
            }
            
            if (fileName != null && !fileName.matches(".*\\.(pdf|docx)$")) {
                log.warn("Unsupported file type: {}", fileName);
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", "Only PDF and DOCX files are supported"));
            }
            
            Map<String, Object> result = resumeAnalysisService.analyzeWithAllModels(request, httpRequest);
            log.info("Multi-model analysis completed successfully for file: {}", fileName);
            
            return ResponseEntity.ok(ApiResponse.success("Multi-model analysis completed", result));
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request - File: {}, Error: {}", fileName, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid Request", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error analyzing with all models - File: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Server Error", "Error analyzing resume: " + e.getMessage()));
        }
    }
}