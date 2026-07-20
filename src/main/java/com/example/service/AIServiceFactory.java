package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AIServiceFactory {
    
    private static final Logger log = LoggerFactory.getLogger(AIServiceFactory.class);
    
    private final Map<String, AIService> serviceMap = new ConcurrentHashMap<>();
    
    @Autowired
    public AIServiceFactory(GrokService grokService, 
                           GeminiService geminiService, 
                           OpenRouterService openRouterService) {
        serviceMap.put("grok", grokService);
        serviceMap.put("gemini", geminiService);
        serviceMap.put("openrouter", openRouterService);
        log.info("Initialized AI Service Factory with providers: {}", serviceMap.keySet());
    }
    
    public AIService getService(String provider) {
        AIService service = serviceMap.get(provider.toLowerCase());
        if (service == null) {
            log.error("Unsupported AI provider: {}", provider);
            throw new IllegalArgumentException("Unsupported AI provider: " + provider);
        }
        log.debug("Selected AI provider: {}", provider);
        return service;
    }
    
    public Map<String, AIService> getAllServices() {
        return new HashMap<>(serviceMap);
    }
}