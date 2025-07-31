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
public class TeamMemberDashboardDTO {
    
    // Personal Overview
    private PersonalOverviewDTO personalOverview;
    
    // Assigned Tasks
    private List<AssignedTaskDTO> assignedTasks;
    
    // Recent Activities
    private List<ActivityDTO> recentActivities;
    
    // Time Tracking
    private TimeTrackingDTO timeTracking;
    
    // Performance Metrics
    private PersonalPerformanceDTO personalPerformance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalOverviewDTO {
        private Long totalTasks;
        private Long completedTasks;
        private Long inProgressTasks;
        private Long overdueTasks;
        private BigDecimal completionRate;
        private Long activeProjects;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedTaskDTO {
        private Long id;
        private String title;
        private String status;
        private String priority;
        private String projectName;
        private LocalDateTime dueDate;
        private BigDecimal progress;
        private Double estimatedHours;
        private Double loggedHours;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeTrackingDTO {
        private Double totalHoursToday;
        private Double totalHoursThisWeek;
        private Double totalHoursThisMonth;
        private Double averageHoursPerDay;
        private List<DailyTimeEntryDTO> recentTimeEntries;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalPerformanceDTO {
        private BigDecimal productivityScore;
        private Long tasksCompletedThisMonth;
        private BigDecimal averageTaskCompletionTime;
        private BigDecimal onTimeDeliveryRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyTimeEntryDTO {
        private LocalDateTime date;
        private Double hours;
        private String taskTitle;
        private String projectName;
    }
}