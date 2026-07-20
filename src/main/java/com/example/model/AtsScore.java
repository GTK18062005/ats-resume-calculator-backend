package com.example.model;

import java.util.Map;

public class AtsScore {
    private double overallScore;
    private double keywordMatchScore;
    private double formattingScore;
    private double readabilityScore;
    private double sectionCompletenessScore;
    private double experienceMatchScore;
    private double educationMatchScore;
    private double skillsMatchScore;
    private Map<String, Object> detailedAnalysis;
    private String feedback;
    private String modelUsed;
    private long processingTime;

    // Getters and Setters
    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getKeywordMatchScore() {
        return keywordMatchScore;
    }

    public void setKeywordMatchScore(double keywordMatchScore) {
        this.keywordMatchScore = keywordMatchScore;
    }

    public double getFormattingScore() {
        return formattingScore;
    }

    public void setFormattingScore(double formattingScore) {
        this.formattingScore = formattingScore;
    }

    public double getReadabilityScore() {
        return readabilityScore;
    }

    public void setReadabilityScore(double readabilityScore) {
        this.readabilityScore = readabilityScore;
    }

    public double getSectionCompletenessScore() {
        return sectionCompletenessScore;
    }

    public void setSectionCompletenessScore(double sectionCompletenessScore) {
        this.sectionCompletenessScore = sectionCompletenessScore;
    }

    public double getExperienceMatchScore() {
        return experienceMatchScore;
    }

    public void setExperienceMatchScore(double experienceMatchScore) {
        this.experienceMatchScore = experienceMatchScore;
    }

    public double getEducationMatchScore() {
        return educationMatchScore;
    }

    public void setEducationMatchScore(double educationMatchScore) {
        this.educationMatchScore = educationMatchScore;
    }

    public double getSkillsMatchScore() {
        return skillsMatchScore;
    }

    public void setSkillsMatchScore(double skillsMatchScore) {
        this.skillsMatchScore = skillsMatchScore;
    }

    public Map<String, Object> getDetailedAnalysis() {
        return detailedAnalysis;
    }

    public void setDetailedAnalysis(Map<String, Object> detailedAnalysis) {
        this.detailedAnalysis = detailedAnalysis;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    // Helper method to calculate weighted overall score
    public void calculateOverallScore() {
        if (overallScore == 0) {
            double weightedScore = (keywordMatchScore * 0.30) +
                                  (formattingScore * 0.10) +
                                  (readabilityScore * 0.10) +
                                  (sectionCompletenessScore * 0.10) +
                                  (experienceMatchScore * 0.20) +
                                  (educationMatchScore * 0.10) +
                                  (skillsMatchScore * 0.10);
            this.overallScore = Math.round(weightedScore * 100.0) / 100.0;
        }
    }
}