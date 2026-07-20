package com.example.service;

import com.example.model.AtsScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenRouterService implements AIService {
    
    private static final Logger log = LoggerFactory.getLogger(OpenRouterService.class);
    
    @Value("${ai.openrouter.api-key}")
    private String apiKey;
    
    @Value("${ai.openrouter.url}")
    private String apiUrl;
    
    @Value("${ai.openrouter.model}")
    private String model;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public OpenRouterService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public AtsScore analyzeResume(String resumeText, String jobDescription) {
        long startTime = System.currentTimeMillis();
        
        try {
            String prompt = buildPrompt(resumeText, jobDescription);
            String response = callOpenRouterAPI(prompt);
            AtsScore score = parseResponse(response);
            score.setModelUsed("OpenRouter (" + model + ")");
            score.setProcessingTime(System.currentTimeMillis() - startTime);
            score.calculateOverallScore();
            return score;
        } catch (Exception e) {
            log.error("Error calling OpenRouter API: ", e);
            return getFallbackScore("OpenRouter API Error: " + e.getMessage());
        }
    }
    
    private String buildPrompt(String resumeText, String jobDescription) {
        return """
            You are an expert ATS resume analyzer. Analyze the resume against the job description.
            Return ONLY JSON, no additional text.
            
            Job Description: """ + jobDescription + """
            
            Resume: """ + resumeText + """
            
            JSON Structure:
            {
                "keywordMatchScore": 0-100,
                "formattingScore": 0-100,
                "readabilityScore": 0-100,
                "sectionCompletenessScore": 0-100,
                "experienceMatchScore": 0-100,
                "educationMatchScore": 0-100,
                "skillsMatchScore": 0-100,
                "overallScore": 0-100,
                "feedback": "string",
                "detailedAnalysis": {
                    "missingKeywords": ["list"],
                    "sectionsFound": ["list"],
                    "strengths": ["list"],
                    "improvements": ["list"]
                }
            }
            """;
    }
    
    private String callOpenRouterAPI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "http://localhost:8080");
        headers.set("X-Title", "ATS Resume Calculator");
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "You are an ATS expert."),
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.3);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        return response.getBody();
    }
    
    private AtsScore parseResponse(String response) {
        try {
            Map<String, Object> openRouterResponse = objectMapper.readValue(response, Map.class);
            Map<String, Object> choice = (Map<String, Object>) ((List<?>) 
                openRouterResponse.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            String content = (String) message.get("content");
            
            String jsonContent = extractJSON(content);
            return objectMapper.readValue(jsonContent, AtsScore.class);
        } catch (Exception e) {
            log.error("Error parsing OpenRouter response: ", e);
            return getFallbackScore("Parse error");
        }
    }
    
    private String extractJSON(String content) {
        int start = content.indexOf("{");
        int end = content.lastIndexOf("}") + 1;
        return content.substring(start, end);
    }
    
    private AtsScore getFallbackScore(String error) {
        AtsScore score = new AtsScore();
        score.setOverallScore(0);
        score.setKeywordMatchScore(0);
        score.setFormattingScore(0);
        score.setReadabilityScore(0);
        score.setSectionCompletenessScore(0);
        score.setExperienceMatchScore(0);
        score.setEducationMatchScore(0);
        score.setSkillsMatchScore(0);
        score.setFeedback("Error: " + error);
        score.setModelUsed("Fallback");
        return score;
    }
    
    @Override
    public String getProviderName() {
        return "openrouter";
    }
}