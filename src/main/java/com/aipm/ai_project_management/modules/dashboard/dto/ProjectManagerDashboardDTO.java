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
public class ProjectManagerDashboardDTO {
    
    // Project Overview
    private ProjectOverviewDTO projectOverview;
    
    // Team Performance
    private TeamPerformanceDTO teamPerformance;
    
    // Task Management
    private TaskManagementDTO taskManagement;
    
    // Recent Projects
    private List<ProjectSummaryDTO> recentProjects;
    
    // Upcoming Deadlines
    private List<UpcomingDeadlineDTO> upcomingDeadlines;
    
    // AI Insights
    private List<AIInsightDTO> aiInsights;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectOverviewDTO {
        private Long totalProjects;
        private Long activeProjects;
        private Long completedProjects;
        private Long overdueProjects;
        private BigDecimal averageProgress;
        private BigDecimal totalBudget;
        private BigDecimal totalSpent;
        private BigDecimal averageHealthScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamPerformanceDTO {
        private Long totalTeamMembers;
        private BigDecimal averageProductivity;
        private Long tasksCompleted;
        private BigDecimal averageTaskCompletionTime;
        private List<TopPerformerDTO> topPerformers;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskManagementDTO {
        private Long totalTasks;
        private Long completedTasks;
        private Long inProgressTasks;
        private Long overdueTasks;
        private Long tasksAssignedToday;
        private BigDecimal completionRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectSummaryDTO {
        private Long id;
        private String name;
        private String status;
        private BigDecimal progress;
        private LocalDateTime deadline;
        private String clientName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingDeadlineDTO {
        private Long projectId;
        private String projectName;
        private Long taskId;
        private String taskTitle;
        private LocalDateTime deadline;
        private String type; // PROJECT, TASK, MILESTONE
        private String priority;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopPerformerDTO {
        private Long userId;
        private String userName;
        private Long tasksCompleted;
        private BigDecimal productivityScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIInsightDTO {
        private String type;
        private String title;
        private String description;
        private String severity;
        private LocalDateTime createdAt;
    }
}