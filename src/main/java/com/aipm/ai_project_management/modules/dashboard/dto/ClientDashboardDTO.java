package com.aipm.ai_project_management.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDashboardDTO {
    
    // Project Overview
    private ClientProjectOverviewDTO projectOverview;
    
    // Active Projects
    private List<ClientProjectDTO> activeProjects;
    
    // Recent Updates
    private List<ProjectUpdateDTO> recentUpdates;
    
    // Budget Summary
    private BudgetSummaryDTO budgetSummary;
    
    // Support & Communication
    private SupportSummaryDTO supportSummary;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientProjectOverviewDTO {
        private Long totalProjects;
        private Long activeProjects;
        private Long completedProjects;
        private Long onHoldProjects;
        private BigDecimal overallProgress;
        private BigDecimal averageHealthScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientProjectDTO {
        private Long id;
        private String name;
        private String status;
        private BigDecimal progress;
        private LocalDateTime startDate;
        private LocalDateTime expectedEndDate;
        private String managerName;
        private BigDecimal budget;
        private BigDecimal spent;
        private BigDecimal healthScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectUpdateDTO {
        private Long projectId;
        private String projectName;
        private String updateType; // MILESTONE_COMPLETED, TASK_COMPLETED, STATUS_CHANGE, etc.
        private String description;
        private LocalDateTime timestamp;
        private String updatedBy;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetSummaryDTO {
        private BigDecimal totalBudget;
        private BigDecimal totalSpent;
        private BigDecimal remainingBudget;
        private BigDecimal budgetUtilization;
        private List<ProjectBudgetDTO> projectBudgets;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectBudgetDTO {
        private Long projectId;
        private String projectName;
        private BigDecimal budget;
        private BigDecimal spent;
        private BigDecimal remaining;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SupportSummaryDTO {
        private Long openTickets;
        private Long closedTickets;
        private Long pendingApprovals;
        private LocalDateTime lastCommunication;
        private String projectManagerContact;
    }
}