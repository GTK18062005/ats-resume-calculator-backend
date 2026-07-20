package com.example.model;

import java.util.Map;

public class ResumeResponse {
    private AtsScore score;
    private String resumeText;
    private String fileName;
    private String fileType;
    private int pageCount;
    private int wordCount;
    private String modelUsed;
    private long processingTime;
    private String timestamp;
    private Map<String, AtsScore> allScores;
    private Map<String, String> modelSuggestions;
    private String bestModel;
    private JobDescriptionAnalysis jobDescriptionAnalysis;
    private ResumeSections sectionsFound;
    private Recommendations recommendations;

    // Getters and Setters
    public AtsScore getScore() {
        return score;
    }

    public void setScore(AtsScore score) {
        this.score = score;
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, AtsScore> getAllScores() {
        return allScores;
    }

    public void setAllScores(Map<String, AtsScore> allScores) {
        this.allScores = allScores;
    }

    public Map<String, String> getModelSuggestions() {
        return modelSuggestions;
    }

    public void setModelSuggestions(Map<String, String> modelSuggestions) {
        this.modelSuggestions = modelSuggestions;
    }

    public String getBestModel() {
        return bestModel;
    }

    public void setBestModel(String bestModel) {
        this.bestModel = bestModel;
    }

    public JobDescriptionAnalysis getJobDescriptionAnalysis() {
        return jobDescriptionAnalysis;
    }

    public void setJobDescriptionAnalysis(JobDescriptionAnalysis jobDescriptionAnalysis) {
        this.jobDescriptionAnalysis = jobDescriptionAnalysis;
    }

    public ResumeSections getSectionsFound() {
        return sectionsFound;
    }

    public void setSectionsFound(ResumeSections sectionsFound) {
        this.sectionsFound = sectionsFound;
    }

    public Recommendations getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Recommendations recommendations) {
        this.recommendations = recommendations;
    }

    // Inner class: JobDescriptionAnalysis
    public static class JobDescriptionAnalysis {
        private String jobTitle;
        private String industry;
        private int requiredYearsExperience;
        private String[] keySkills;
        private String[] responsibilities;
        private String[] preferredQualifications;

        // Getters and Setters
        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public int getRequiredYearsExperience() {
            return requiredYearsExperience;
        }

        public void setRequiredYearsExperience(int requiredYearsExperience) {
            this.requiredYearsExperience = requiredYearsExperience;
        }

        public String[] getKeySkills() {
            return keySkills;
        }

        public void setKeySkills(String[] keySkills) {
            this.keySkills = keySkills;
        }

        public String[] getResponsibilities() {
            return responsibilities;
        }

        public void setResponsibilities(String[] responsibilities) {
            this.responsibilities = responsibilities;
        }

        public String[] getPreferredQualifications() {
            return preferredQualifications;
        }

        public void setPreferredQualifications(String[] preferredQualifications) {
            this.preferredQualifications = preferredQualifications;
        }
    }

    // Inner class: ResumeSections
    public static class ResumeSections {
        private boolean hasContactInfo;
        private boolean hasSummary;
        private boolean hasExperience;
        private boolean hasEducation;
        private boolean hasSkills;
        private boolean hasProjects;
        private boolean hasCertifications;
        private boolean hasAchievements;
        private int totalSections;
        private int missingSections;
        private String[] sectionsFound;
        private String[] sectionsMissing;

        // Getters and Setters
        public boolean isHasContactInfo() {
            return hasContactInfo;
        }

        public void setHasContactInfo(boolean hasContactInfo) {
            this.hasContactInfo = hasContactInfo;
        }

        public boolean isHasSummary() {
            return hasSummary;
        }

        public void setHasSummary(boolean hasSummary) {
            this.hasSummary = hasSummary;
        }

        public boolean isHasExperience() {
            return hasExperience;
        }

        public void setHasExperience(boolean hasExperience) {
            this.hasExperience = hasExperience;
        }

        public boolean isHasEducation() {
            return hasEducation;
        }

        public void setHasEducation(boolean hasEducation) {
            this.hasEducation = hasEducation;
        }

        public boolean isHasSkills() {
            return hasSkills;
        }

        public void setHasSkills(boolean hasSkills) {
            this.hasSkills = hasSkills;
        }

        public boolean isHasProjects() {
            return hasProjects;
        }

        public void setHasProjects(boolean hasProjects) {
            this.hasProjects = hasProjects;
        }

        public boolean isHasCertifications() {
            return hasCertifications;
        }

        public void setHasCertifications(boolean hasCertifications) {
            this.hasCertifications = hasCertifications;
        }

        public boolean isHasAchievements() {
            return hasAchievements;
        }

        public void setHasAchievements(boolean hasAchievements) {
            this.hasAchievements = hasAchievements;
        }

        public int getTotalSections() {
            return totalSections;
        }

        public void setTotalSections(int totalSections) {
            this.totalSections = totalSections;
        }

        public int getMissingSections() {
            return missingSections;
        }

        public void setMissingSections(int missingSections) {
            this.missingSections = missingSections;
        }

        public String[] getSectionsFound() {
            return sectionsFound;
        }

        public void setSectionsFound(String[] sectionsFound) {
            this.sectionsFound = sectionsFound;
        }

        public String[] getSectionsMissing() {
            return sectionsMissing;
        }

        public void setSectionsMissing(String[] sectionsMissing) {
            this.sectionsMissing = sectionsMissing;
        }
    }

    // Inner class: Recommendations
    public static class Recommendations {
        private String[] highPriority;
        private String[] mediumPriority;
        private String[] lowPriority;
        private String[] keywordsToAdd;
        private String[] formattingIssues;
        private String[] contentSuggestions;
        private String summary;

        // Getters and Setters
        public String[] getHighPriority() {
            return highPriority;
        }

        public void setHighPriority(String[] highPriority) {
            this.highPriority = highPriority;
        }

        public String[] getMediumPriority() {
            return mediumPriority;
        }

        public void setMediumPriority(String[] mediumPriority) {
            this.mediumPriority = mediumPriority;
        }

        public String[] getLowPriority() {
            return lowPriority;
        }

        public void setLowPriority(String[] lowPriority) {
            this.lowPriority = lowPriority;
        }

        public String[] getKeywordsToAdd() {
            return keywordsToAdd;
        }

        public void setKeywordsToAdd(String[] keywordsToAdd) {
            this.keywordsToAdd = keywordsToAdd;
        }

        public String[] getFormattingIssues() {
            return formattingIssues;
        }

        public void setFormattingIssues(String[] formattingIssues) {
            this.formattingIssues = formattingIssues;
        }

        public String[] getContentSuggestions() {
            return contentSuggestions;
        }

        public void setContentSuggestions(String[] contentSuggestions) {
            this.contentSuggestions = contentSuggestions;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
}