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
public class GrokService implements AIService {
    
    private static final Logger log = LoggerFactory.getLogger(GrokService.class);
    
    @Value("${ai.grok.api-key}")
    private String apiKey;
    
    @Value("${ai.grok.url}")
    private String apiUrl;
    
    @Value("${ai.grok.model}")
    private String model;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public GrokService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public AtsScore analyzeResume(String resumeText, String jobDescription) {
        long startTime = System.currentTimeMillis();
        
        try {
            String prompt = buildPrompt(resumeText, jobDescription);
            String response = callGrokAPI(prompt);
            AtsScore score = parseResponse(response);
            score.setModelUsed("Groq (" + model + ")");
            score.setProcessingTime(System.currentTimeMillis() - startTime);
            score.calculateOverallScore();
            return score;
        } catch (Exception e) {
            log.error("Error calling Groq API: ", e);
            return getFallbackScore("Groq API Error: " + e.getMessage());
        }
    }
    
    private String buildPrompt(String resumeText, String jobDescription) {
        return """
            You are an expert ATS (Applicant Tracking System) resume analyzer. Analyze the following resume against the job description and provide a detailed score.

            Job Description:
            """ + jobDescription + """
            
            Resume:
            """ + resumeText + """
            
            Please provide a JSON response with the following structure:
            {
                "keywordMatchScore": 0-100 (how well keywords match),
                "formattingScore": 0-100 (ATS-friendly formatting),
                "readabilityScore": 0-100 (clarity and structure),
                "sectionCompletenessScore": 0-100 (all required sections present),
                "experienceMatchScore": 0-100 (experience relevance),
                "educationMatchScore": 0-100 (education match),
                "skillsMatchScore": 0-100 (skills alignment),
                "overallScore": 0-100 (weighted average),
                "feedback": "Brief feedback with improvement suggestions",
                "detailedAnalysis": {
                    "missingKeywords": ["list of missing keywords"],
                    "sectionsFound": ["list of sections found"],
                    "strengths": ["list of strengths"],
                    "improvements": ["list of improvements"],
                    "suggestedKeywords": ["keywords to add"],
                    "formattingIssues": ["issues found"]
                }
            }
            
            Return ONLY the JSON object, no additional text.
            """;
    }
    
    private String callGrokAPI(String prompt) {
        // Groq API endpoint
        String url = "https://api.groq.com/openai/v1/chat/completions";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile"); // Use the recommended Groq model
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "You are an ATS expert resume analyzer. Return only valid JSON."),
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", 1500);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        log.info("Calling Groq API with model: llama-3.3-70b-versatile");
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        log.info("Groq API response received");
        return response.getBody();
    }
    
    private AtsScore parseResponse(String response) {
        try {
            Map<String, Object> groqResponse = objectMapper.readValue(response, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) groqResponse.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");
            
            String jsonContent = extractJSON(content);
            return objectMapper.readValue(jsonContent, AtsScore.class);
        } catch (Exception e) {
            log.error("Error parsing Groq response: ", e);
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
        return "groq";
    }
}