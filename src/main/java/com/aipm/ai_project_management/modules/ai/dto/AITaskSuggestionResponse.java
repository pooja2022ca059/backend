package com.aipm.ai_project_management.modules.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AITaskSuggestionResponse {
    private List<TaskSuggestion> suggestions;
    private BigDecimal confidence;
    private String reasoning;
    private LocalDateTime generatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskSuggestion {
        private String title;
        private String description;
        private String priority; // LOW, MEDIUM, HIGH, CRITICAL
        private Double estimatedHours;
        private List<String> dependencies;
        private List<String> suggestedAssignees;
        private String category;
        private Map<String, Object> metadata;
    }
}