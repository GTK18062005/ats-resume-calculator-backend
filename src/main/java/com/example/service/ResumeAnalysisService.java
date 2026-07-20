package com.example.service;

import com.example.model.AtsScore;
import com.example.model.ResumeRequest;
import com.example.model.UserResume;
import com.example.repository.UserResumeRepository;
import com.example.utils.FileParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeAnalysisService {
    
    private static final Logger log = LoggerFactory.getLogger(ResumeAnalysisService.class);
    
    @Autowired
    private FileParserUtil fileParserUtil;
    
    @Autowired
    private AIServiceFactory aiServiceFactory;
    
    @Autowired
    private UserResumeRepository userResumeRepository;
    
    private final ObjectMapper objectMapper;
    
    public ResumeAnalysisService() {
        this.objectMapper = new ObjectMapper();
    }
    
    public Map<String, Object> analyzeResume(ResumeRequest request, HttpServletRequest httpRequest) throws Exception {
        log.info("Starting resume analysis for file: {}", 
            request.getResumeFile().getOriginalFilename());
        
        // Extract text from resume
        MultipartFile file = request.getResumeFile();
        String resumeText = fileParserUtil.extractText(file);
        log.info("Successfully extracted text from resume. Length: {} characters", resumeText.length());
        
        // Get AI service
        String provider = request.getModelProvider();
        if (provider == null || provider.isEmpty()) {
            provider = "grok"; // Default
            log.info("No provider specified, using default: grok");
        }
        
        AIService aiService = aiServiceFactory.getService(provider);
        log.info("Analyzing resume with: {}", aiService.getProviderName());
        
        // Perform analysis
        long startTime = System.currentTimeMillis();
        AtsScore score = aiService.analyzeResume(resumeText, request.getJobDescription());
        long processingTime = System.currentTimeMillis() - startTime;
        log.info("Analysis completed in {} ms with score: {}", processingTime, score.getOverallScore());
        
        // Save to database
        UserResume savedResume = saveToDatabase(resumeText, request.getJobDescription(), score, file, httpRequest);
        log.info("Saved analysis result to database with ID: {}", savedResume != null ? savedResume.getId() : "null");
        
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("resumeText", resumeText);
        response.put("fileName", file.getOriginalFilename());
        response.put("fileSize", file.getSize());
        response.put("fileType", file.getContentType());
        response.put("modelUsed", aiService.getProviderName());
        response.put("processingTimeMs", processingTime);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("savedRecordId", savedResume != null ? savedResume.getId() : null);
        
        return response;
    }
    
    public Map<String, Object> analyzeWithAllModels(ResumeRequest request, HttpServletRequest httpRequest) throws Exception {
        MultipartFile file = request.getResumeFile();
        log.info("Starting multi-model analysis for file: {}", file.getOriginalFilename());
        
        String resumeText = fileParserUtil.extractText(file);
        String jobDescription = request.getJobDescription();
        log.info("Extracted resume text length: {} characters", resumeText.length());
        
        Map<String, AtsScore> allScores = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        
        // Analyze with all available models
        Map<String, AIService> services = aiServiceFactory.getAllServices();
        log.info("Analyzing with {} providers: {}", services.size(), services.keySet());
        
        for (Map.Entry<String, AIService> entry : services.entrySet()) {
            String providerName = entry.getKey();
            try {
                long startTime = System.currentTimeMillis();
                AIService service = entry.getValue();
                AtsScore score = service.analyzeResume(resumeText, jobDescription);
                long processingTime = System.currentTimeMillis() - startTime;
                
                allScores.put(providerName, score);
                log.info("{} analysis completed in {} ms with score: {}", 
                    providerName, processingTime, score.getOverallScore());
                
                // Save each result
                saveToDatabase(resumeText, jobDescription, score, file, httpRequest);
                
            } catch (Exception e) {
                log.error("Error analyzing with {}: ", providerName, e);
                allScores.put(providerName, null);
                errors.put(providerName, e.getMessage());
            }
        }
        
        // Find best model based on overall score
        String bestModel = findBestModel(allScores);
        if (bestModel != null) {
            log.info("Best performing model: {} with score: {}", 
                bestModel, allScores.get(bestModel).getOverallScore());
        }
        
        response.put("allScores", allScores);
        response.put("errors", errors);
        response.put("bestModel", bestModel);
        response.put("resumeText", resumeText);
        response.put("fileName", file.getOriginalFilename());
        response.put("fileSize", file.getSize());
        response.put("fileType", file.getContentType());
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("totalModelsAnalyzed", services.size());
        response.put("successfulModels", allScores.values().stream().filter(s -> s != null).count());
        
        return response;
    }
    
    private String findBestModel(Map<String, AtsScore> allScores) {
        String bestModel = null;
        double highestScore = -1;
        
        for (Map.Entry<String, AtsScore> entry : allScores.entrySet()) {
            AtsScore score = entry.getValue();
            if (score != null && score.getOverallScore() > highestScore) {
                highestScore = score.getOverallScore();
                bestModel = entry.getKey();
            }
        }
        
        return bestModel;
    }
    
    private UserResume saveToDatabase(String resumeText, String jobDescription, 
                               AtsScore score, MultipartFile file, HttpServletRequest request) {
        try {
            UserResume userResume = new UserResume();
            userResume.setResumeText(resumeText);
            userResume.setJobDescription(jobDescription);
            
            // Serialize AtsScore to JSON
            String scoreJson = objectMapper.writeValueAsString(score);
            userResume.setAtsScore(scoreJson);
            
            // Set individual score fields for easier querying
            userResume.setOverallScore(score.getOverallScore());
            userResume.setKeywordMatchScore(score.getKeywordMatchScore());
            userResume.setFormattingScore(score.getFormattingScore());
            userResume.setReadabilityScore(score.getReadabilityScore());
            userResume.setSectionCompletenessScore(score.getSectionCompletenessScore());
            userResume.setExperienceMatchScore(score.getExperienceMatchScore());
            userResume.setEducationMatchScore(score.getEducationMatchScore());
            userResume.setSkillsMatchScore(score.getSkillsMatchScore());
            userResume.setFeedback(score.getFeedback());
            
            userResume.setModelUsed(score.getModelUsed());
            userResume.setFileName(file.getOriginalFilename());
            userResume.setFileType(file.getContentType());
            userResume.setProcessedAt(LocalDateTime.now());
            
            // Get client IP
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // In case of multiple IPs (e.g., through proxies), take the first one
            if (ipAddress != null && ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0].trim();
            }
            userResume.setIpAddress(ipAddress);
            
            UserResume savedResume = userResumeRepository.save(userResume);
            log.debug("Saved resume analysis to database with ID: {}", savedResume.getId());
            return savedResume;
            
        } catch (Exception e) {
            log.error("Error saving to database: ", e);
            return null;
        }
    }
    
    // Additional utility method to get analysis history
    public Map<String, Object> getAnalysisHistory(String ipAddress) {
        try {
            // You can implement this to fetch history from database
            Map<String, Object> response = new HashMap<>();
            response.put("message", "History retrieval not yet implemented");
            return response;
        } catch (Exception e) {
            log.error("Error retrieving history: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve history: " + e.getMessage());
            return errorResponse;
        }
    }
}