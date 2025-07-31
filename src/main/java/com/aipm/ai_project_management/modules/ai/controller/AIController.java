package com.aipm.ai_project_management.modules.ai.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.ai.dto.*;
import com.aipm.ai_project_management.modules.ai.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Integration", description = "AI-powered project management features")
public class AIController {
    
    private final AIService aiService;
    
    @PostMapping("/predict-risk")
    @Operation(summary = "Predict project risk using AI")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<AIRiskPredictionResponse>> predictProjectRisk(
            @Valid @RequestBody AIRiskPredictionRequest request) {
        
        AIRiskPredictionResponse prediction = aiService.predictProjectRisk(request);
        return ResponseEntity.ok(ApiResponse.success("Risk prediction completed successfully", prediction));
    }
    
    @PostMapping("/task-suggestions")
    @Operation(summary = "Get AI-powered task suggestions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<AITaskSuggestionResponse>> getTaskSuggestions(
            @Valid @RequestBody AITaskSuggestionRequest request) {
        
        AITaskSuggestionResponse suggestions = aiService.getTaskSuggestions(request);
        return ResponseEntity.ok(ApiResponse.success("Task suggestions generated successfully", suggestions));
    }
    
    @GetMapping("/insights/project/{projectId}")
    @Operation(summary = "Get AI insights for a specific project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canAccessProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<List<AIInsightDTO>>> getProjectInsights(@PathVariable Long projectId) {
        List<AIInsightDTO> insights = aiService.getProjectInsights(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project insights retrieved successfully", insights));
    }
    
    @GetMapping("/insights/user/{userId}")
    @Operation(summary = "Get AI insights for a specific user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @userSecurityService.isSameUser(#userId, authentication.name)")
    public ResponseEntity<ApiResponse<List<AIInsightDTO>>> getUserInsights(@PathVariable Long userId) {
        List<AIInsightDTO> insights = aiService.getUserInsights(userId);
        return ResponseEntity.ok(ApiResponse.success("User insights retrieved successfully", insights));
    }
    
    @GetMapping("/insights/system")
    @Operation(summary = "Get system-wide AI insights")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AIInsightDTO>>> getSystemInsights() {
        List<AIInsightDTO> insights = aiService.getSystemInsights();
        return ResponseEntity.ok(ApiResponse.success("System insights retrieved successfully", insights));
    }
    
    @GetMapping("/health")
    @Operation(summary = "Check AI service health")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkAIServiceHealth() {
        boolean isHealthy = aiService.isAIServiceHealthy();
        
        Map<String, Object> healthStatus = Map.of(
            "healthy", isHealthy,
            "status", isHealthy ? "UP" : "DOWN",
            "message", isHealthy ? "AI service is operational" : "AI service is unavailable"
        );
        
        return ResponseEntity.ok(ApiResponse.success("AI service health check completed", healthStatus));
    }
}