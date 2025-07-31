package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.*;

import java.util.List;

public interface AIService {
    
    /**
     * Predict project risk based on current status and metrics
     */
    AIRiskPredictionResponse predictProjectRisk(AIRiskPredictionRequest request);
    
    /**
     * Get AI-powered task suggestions for a project
     */
    AITaskSuggestionResponse getTaskSuggestions(AITaskSuggestionRequest request);
    
    /**
     * Get AI insights for a specific project
     */
    List<AIInsightDTO> getProjectInsights(Long projectId);
    
    /**
     * Get AI insights for a specific user/team member
     */
    List<AIInsightDTO> getUserInsights(Long userId);
    
    /**
     * Get system-wide AI insights for admin dashboard
     */
    List<AIInsightDTO> getSystemInsights();
    
    /**
     * Health check for AI service
     */
    boolean isAIServiceHealthy();
}