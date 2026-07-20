package com.example.model;

import org.springframework.web.multipart.MultipartFile;

public class ResumeRequest {
    private MultipartFile resumeFile;
    private String jobDescription;
    private String modelProvider;

    // Getters and Setters
    public MultipartFile getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(MultipartFile resumeFile) {
        this.resumeFile = resumeFile;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getModelProvider() {
        return modelProvider;
    }

    public void setModelProvider(String modelProvider) {
        this.modelProvider = modelProvider;
    }
}