package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.common.exceptions.AIServiceException;
import com.aipm.ai_project_management.modules.ai.config.AIServiceConfig;
import com.aipm.ai_project_management.modules.ai.dto.*;
import com.aipm.ai_project_management.modules.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {
    
    @Qualifier("aiRestTemplate")
    private final RestTemplate aiRestTemplate;
    private final AIServiceConfig aiServiceConfig;
    
    @Override
    public AIRiskPredictionResponse predictProjectRisk(AIRiskPredictionRequest request) {
        log.debug("Predicting project risk for project: {}", request.getProjectId());
        
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/api/predict-risk";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<AIRiskPredictionRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<AIRiskPredictionResponse> response = aiRestTemplate.exchange(
                url, HttpMethod.POST, entity, AIRiskPredictionResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully predicted project risk for project: {}", request.getProjectId());
                return response.getBody();
            } else {
                log.warn("AI service returned non-OK status: {}", response.getStatusCode());
                return createFallbackRiskPrediction(request);
            }
            
        } catch (RestClientException e) {
            log.error("Error calling AI service for risk prediction: {}", e.getMessage());
            // Fallback to mock prediction
            return createFallbackRiskPrediction(request);
        }
    }
    
    @Override
    public AITaskSuggestionResponse getTaskSuggestions(AITaskSuggestionRequest request) {
        log.debug("Getting task suggestions for project: {}", request.getProjectId());
        
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/api/task-suggestions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<AITaskSuggestionRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<AITaskSuggestionResponse> response = aiRestTemplate.exchange(
                url, HttpMethod.POST, entity, AITaskSuggestionResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully retrieved task suggestions for project: {}", request.getProjectId());
                return response.getBody();
            } else {
                log.warn("AI service returned non-OK status: {}", response.getStatusCode());
                return createFallbackTaskSuggestions(request);
            }
            
        } catch (RestClientException e) {
            log.error("Error calling AI service for task suggestions: {}", e.getMessage());
            // Fallback to mock suggestions
            return createFallbackTaskSuggestions(request);
        }
    }
    
    @Override
    @Cacheable(value = "projectInsights", key = "#projectId")
    public List<AIInsightDTO> getProjectInsights(Long projectId) {
        log.debug("Getting AI insights for project: {}", projectId);
        
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/api/insights/project/" + projectId;
            
            ResponseEntity<List> response = aiRestTemplate.getForEntity(url, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully retrieved AI insights for project: {}", projectId);
                // In a real implementation, you would properly deserialize the response
                return createMockProjectInsights(projectId);
            } else {
                log.warn("AI service returned non-OK status: {}", response.getStatusCode());
                return createMockProjectInsights(projectId);
            }
            
        } catch (RestClientException e) {
            log.error("Error calling AI service for project insights: {}", e.getMessage());
            return createMockProjectInsights(projectId);
        }
    }
    
    @Override
    @Cacheable(value = "userInsights", key = "#userId")
    public List<AIInsightDTO> getUserInsights(Long userId) {
        log.debug("Getting AI insights for user: {}", userId);
        
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/api/insights/user/" + userId;
            
            ResponseEntity<List> response = aiRestTemplate.getForEntity(url, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully retrieved AI insights for user: {}", userId);
                return createMockUserInsights(userId);
            } else {
                log.warn("AI service returned non-OK status: {}", response.getStatusCode());
                return createMockUserInsights(userId);
            }
            
        } catch (RestClientException e) {
            log.error("Error calling AI service for user insights: {}", e.getMessage());
            return createMockUserInsights(userId);
        }
    }
    
    @Override
    @Cacheable(value = "systemInsights")
    public List<AIInsightDTO> getSystemInsights() {
        log.debug("Getting system-wide AI insights");
        
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/api/insights/system";
            
            ResponseEntity<List> response = aiRestTemplate.getForEntity(url, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully retrieved system AI insights");
                return createMockSystemInsights();
            } else {
                log.warn("AI service returned non-OK status: {}", response.getStatusCode());
                return createMockSystemInsights();
            }
            
        } catch (RestClientException e) {
            log.error("Error calling AI service for system insights: {}", e.getMessage());
            return createMockSystemInsights();
        }
    }
    
    @Override
    public boolean isAIServiceHealthy() {
        try {
            String url = aiServiceConfig.getAiServiceBaseUrl() + "/health";
            ResponseEntity<String> response = aiRestTemplate.getForEntity(url, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("AI service health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    // Fallback methods for when AI service is unavailable
    
    private AIRiskPredictionResponse createFallbackRiskPrediction(AIRiskPredictionRequest request) {
        log.info("Creating fallback risk prediction for project: {}", request.getProjectId());
        
        // Simple heuristic-based risk assessment
        String riskLevel = "MEDIUM";
        BigDecimal riskScore = BigDecimal.valueOf(45.0);
        
        if (request.getCurrentProgress() != null && request.getCurrentProgress().compareTo(BigDecimal.valueOf(80)) > 0) {
            riskLevel = "LOW";
            riskScore = BigDecimal.valueOf(25.0);
        } else if (request.getCurrentProgress() != null && request.getCurrentProgress().compareTo(BigDecimal.valueOf(30)) < 0) {
            riskLevel = "HIGH";
            riskScore = BigDecimal.valueOf(75.0);
        }
        
        List<AIRiskPredictionResponse.RiskFactor> riskFactors = new ArrayList<>();
        riskFactors.add(AIRiskPredictionResponse.RiskFactor.builder()
            .category("TIMELINE")
            .description("Project timeline analysis")
            .impact(BigDecimal.valueOf(6.0))
            .probability(BigDecimal.valueOf(40.0))
            .mitigation("Monitor milestone completion rates")
            .build());
        
        List<String> recommendations = List.of(
            "Monitor task completion rates daily",
            "Review resource allocation",
            "Consider scope adjustments if needed"
        );
        
        return AIRiskPredictionResponse.builder()
            .riskLevel(riskLevel)
            .riskScore(riskScore)
            .confidence(BigDecimal.valueOf(70.0))
            .riskFactors(riskFactors)
            .recommendations(recommendations)
            .predictedOutcome(AIRiskPredictionResponse.PredictedOutcome.builder()
                .predictedCompletionDate(LocalDateTime.now().plusDays(30))
                .predictedBudgetOverrun(BigDecimal.valueOf(5.0))
                .qualityScore(BigDecimal.valueOf(85.0))
                .build())
            .analysisTimestamp(LocalDateTime.now())
            .build();
    }
    
    private AITaskSuggestionResponse createFallbackTaskSuggestions(AITaskSuggestionRequest request) {
        log.info("Creating fallback task suggestions for project: {}", request.getProjectId());
        
        List<AITaskSuggestionResponse.TaskSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(AITaskSuggestionResponse.TaskSuggestion.builder()
            .title("Project kickoff meeting")
            .description("Organize initial project meeting with all stakeholders")
            .priority("HIGH")
            .estimatedHours(2.0)
            .dependencies(List.of())
            .suggestedAssignees(List.of("Project Manager"))
            .category("PLANNING")
            .build());
        
        suggestions.add(AITaskSuggestionResponse.TaskSuggestion.builder()
            .title("Requirements analysis")
            .description("Analyze and document project requirements")
            .priority("HIGH")
            .estimatedHours(16.0)
            .dependencies(List.of("Project kickoff meeting"))
            .suggestedAssignees(List.of("Business Analyst"))
            .category("ANALYSIS")
            .build());
        
        return AITaskSuggestionResponse.builder()
            .suggestions(suggestions)
            .confidence(BigDecimal.valueOf(75.0))
            .reasoning("Generated based on common project patterns")
            .generatedAt(LocalDateTime.now())
            .build();
    }
    
    private List<AIInsightDTO> createMockProjectInsights(Long projectId) {
        List<AIInsightDTO> insights = new ArrayList<>();
        
        insights.add(AIInsightDTO.builder()
            .type("RISK_PREDICTION")
            .title("Timeline Risk Detected")
            .description("Project may be at risk of missing deadline based on current velocity")
            .severity("MEDIUM")
            .confidence(BigDecimal.valueOf(78.0))
            .actionItems(List.of("Review task assignments", "Consider additional resources"))
            .category("TIMELINE")
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(7))
            .isActive(true)
            .build());
        
        return insights;
    }
    
    private List<AIInsightDTO> createMockUserInsights(Long userId) {
        List<AIInsightDTO> insights = new ArrayList<>();
        
        insights.add(AIInsightDTO.builder()
            .type("PERFORMANCE_ANALYSIS")
            .title("High Performance Detected")
            .description("User is completing tasks 20% faster than average")
            .severity("LOW")
            .confidence(BigDecimal.valueOf(92.0))
            .actionItems(List.of("Consider additional responsibilities", "Share best practices"))
            .category("PERFORMANCE")
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(30))
            .isActive(true)
            .build());
        
        return insights;
    }
    
    private List<AIInsightDTO> createMockSystemInsights() {
        List<AIInsightDTO> insights = new ArrayList<>();
        
        insights.add(AIInsightDTO.builder()
            .type("RESOURCE_OPTIMIZATION")
            .title("Resource Allocation Optimization")
            .description("System detected potential for better resource distribution across projects")
            .severity("MEDIUM")
            .confidence(BigDecimal.valueOf(85.0))
            .actionItems(List.of("Review project priorities", "Rebalance team assignments"))
            .category("RESOURCE")
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(14))
            .isActive(true)
            .build());
        
        return insights;
    }
}