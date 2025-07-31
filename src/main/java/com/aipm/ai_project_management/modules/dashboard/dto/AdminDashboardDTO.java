package com.aipm.ai_project_management.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {
    
    // System Overview
    private SystemMetricsDTO systemMetrics;
    
    // Project Statistics
    private ProjectStatsDTO projectStats;
    
    // User Statistics  
    private UserStatsDTO userStats;
    
    // Client Statistics
    private ClientStatsDTO clientStats;
    
    // Task Statistics
    private TaskStatsDTO taskStats;
    
    // Recent Activities
    private List<ActivityDTO> recentActivities;
    
    // Performance Metrics
    private PerformanceMetricsDTO performanceMetrics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemMetricsDTO {
        private Long totalUsers;
        private Long activeUsers;
        private Long totalClients;
        private Long totalProjects;
        private Long totalTasks;
        private BigDecimal systemHealthScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectStatsDTO {
        private Long totalProjects;
        private Long activeProjects;
        private Long completedProjects;
        private Long overdueProjects;
        private BigDecimal averageProgress;
        private BigDecimal totalBudget;
        private BigDecimal totalSpent;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatsDTO {
        private Long totalUsers;
        private Long activeUsers;
        private Long admins;
        private Long projectManagers;
        private Long teamMembers;
        private Long clients;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientStatsDTO {
        private Long totalClients;
        private Long activeClients;
        private Long newClientsThisMonth;
        private BigDecimal averageProjectsPerClient;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskStatsDTO {
        private Long totalTasks;
        private Long completedTasks;
        private Long inProgressTasks;
        private Long overdueTasks;
        private BigDecimal averageCompletionTime;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMetricsDTO {
        private BigDecimal projectSuccessRate;
        private BigDecimal onTimeDeliveryRate;
        private BigDecimal budgetAccuracyRate;
        private BigDecimal teamProductivity;
    }
}