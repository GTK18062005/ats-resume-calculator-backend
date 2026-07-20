package com.example.service;

import com.example.model.AtsScore;

public interface AIService {
    AtsScore analyzeResume(String resumeText, String jobDescription);
    String getProviderName();
}