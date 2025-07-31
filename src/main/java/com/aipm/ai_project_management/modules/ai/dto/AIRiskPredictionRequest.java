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
public class AIRiskPredictionRequest {
    private Long projectId;
    private String projectName;
    private String projectStatus;
    private BigDecimal currentProgress;
    private LocalDateTime startDate;
    private LocalDateTime expectedEndDate;
    private BigDecimal budget;
    private BigDecimal spent;
    private List<TaskSummary> tasks;
    private List<TeamMemberSummary> teamMembers;
    private Map<String, Object> additionalFactors;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskSummary {
        private String status;
        private String priority;
        private LocalDateTime dueDate;
        private BigDecimal progress;
        private Double estimatedHours;
        private Double loggedHours;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamMemberSummary {
        private String role;
        private BigDecimal workload;
        private BigDecimal performance;
    }
}