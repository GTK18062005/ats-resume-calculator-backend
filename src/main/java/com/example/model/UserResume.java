package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_resumes")
public class UserResume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String resumeText;
    
    @Column(columnDefinition = "TEXT")
    private String jobDescription;
    
    @Column(columnDefinition = "LONGTEXT")
    private String atsScore;
    
    private String modelUsed;
    private String fileName;
    private String fileType;
    private Double overallScore;
    private Double keywordMatchScore;
    private Double formattingScore;
    private Double readabilityScore;
    private Double sectionCompletenessScore;
    private Double experienceMatchScore;
    private Double educationMatchScore;
    private Double skillsMatchScore;
    
    @Column(columnDefinition = "LONGTEXT")  // Changed from VARCHAR(255) to LONGTEXT
    private String feedback;
    
    private LocalDateTime processedAt;
    private String ipAddress;

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }
    
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    
    public String getAtsScore() { return atsScore; }
    public void setAtsScore(String atsScore) { this.atsScore = atsScore; }
    
    public String getModelUsed() { return modelUsed; }
    public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
    
    public Double getKeywordMatchScore() { return keywordMatchScore; }
    public void setKeywordMatchScore(Double keywordMatchScore) { this.keywordMatchScore = keywordMatchScore; }
    
    public Double getFormattingScore() { return formattingScore; }
    public void setFormattingScore(Double formattingScore) { this.formattingScore = formattingScore; }
    
    public Double getReadabilityScore() { return readabilityScore; }
    public void setReadabilityScore(Double readabilityScore) { this.readabilityScore = readabilityScore; }
    
    public Double getSectionCompletenessScore() { return sectionCompletenessScore; }
    public void setSectionCompletenessScore(Double sectionCompletenessScore) { this.sectionCompletenessScore = sectionCompletenessScore; }
    
    public Double getExperienceMatchScore() { return experienceMatchScore; }
    public void setExperienceMatchScore(Double experienceMatchScore) { this.experienceMatchScore = experienceMatchScore; }
    
    public Double getEducationMatchScore() { return educationMatchScore; }
    public void setEducationMatchScore(Double educationMatchScore) { this.educationMatchScore = educationMatchScore; }
    
    public Double getSkillsMatchScore() { return skillsMatchScore; }
    public void setSkillsMatchScore(Double skillsMatchScore) { this.skillsMatchScore = skillsMatchScore; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}