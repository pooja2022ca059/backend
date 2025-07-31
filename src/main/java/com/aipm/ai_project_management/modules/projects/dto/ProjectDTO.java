package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private ProjectPriority priority;
    private BigDecimal progress;
    private BigDecimal budget;
    private BigDecimal spent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private BigDecimal aiHealthScore;
    private ProjectPriority aiRiskLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related entities
    private ClientSummaryDTO client;
    private UserSummaryDTO manager;
    private List<ProjectTeamMemberDTO> teamMembers;
    private List<ProjectMilestoneDTO> milestones;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientSummaryDTO {
        private Long id;
        private String name;
        private String companyName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummaryDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
    }
}