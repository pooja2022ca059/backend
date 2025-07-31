package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {
    
    private String name;
    private String description;
    private Long managerId;
    private ProjectStatus status;
    private ProjectPriority priority;
    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private BigDecimal progress;
}