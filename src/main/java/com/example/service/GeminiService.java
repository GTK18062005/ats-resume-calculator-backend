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
public class GeminiService implements AIService {
    
    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    
    @Value("${ai.gemini.api-key}")
    private String apiKey;
    
    @Value("${ai.gemini.url}")
    private String apiUrl;
    
    @Value("${ai.gemini.model}")
    private String model;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public AtsScore analyzeResume(String resumeText, String jobDescription) {
        long startTime = System.currentTimeMillis();
        
        try {
            String prompt = buildPrompt(resumeText, jobDescription);
            String response = callGeminiAPI(prompt);
            AtsScore score = parseResponse(response);
            score.setModelUsed("Gemini (" + model + ")");
            score.setProcessingTime(System.currentTimeMillis() - startTime);
            score.calculateOverallScore();
            return score;
        } catch (Exception e) {
            log.error("Error calling Gemini API: ", e);
            return getFallbackScore("Gemini API Error: " + e.getMessage());
        }
    }
    
    private String buildPrompt(String resumeText, String jobDescription) {
        return """
            You are an expert ATS (Applicant Tracking System) resume analyzer. Analyze the following resume against the job description and provide a detailed score.
            
            IMPORTANT: Return ONLY a valid JSON object, no additional text.
            
            Job Description:
            """ + jobDescription + """
            
            Resume:
            """ + resumeText + """
            
            Return JSON with this exact structure:
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
    
    private String callGeminiAPI(String prompt) throws Exception {
        String url = apiUrl + "/" + model + ":generateContent?key=" + apiKey;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
            Map.of("parts", List.of(Map.of("text", prompt)))
        ));
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        log.info("Calling Gemini API with model: {}", model);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        log.info("Gemini API response received");
        return response.getBody();
    }
    
    private AtsScore parseResponse(String response) {
        try {
            Map<String, Object> geminiResponse = objectMapper.readValue(response, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) geminiResponse.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            String text = (String) parts.get(0).get("text");
            
            String jsonContent = extractJSON(text);
            return objectMapper.readValue(jsonContent, AtsScore.class);
        } catch (Exception e) {
            log.error("Error parsing Gemini response: ", e);
            return getFallbackScore("Parse error: " + e.getMessage());
        }
    }
    
    private String extractJSON(String content) {
        int start = content.indexOf("{");
        int end = content.lastIndexOf("}") + 1;
        if (start >= 0 && end > start) {
            return content.substring(start, end);
        }
        return content;
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
        return "gemini";
    }
}