package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateProjectRequest {
    
    @NotBlank(message = "Project name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    private Long managerId;
    
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PLANNING;
    
    @Builder.Default
    private ProjectPriority priority = ProjectPriority.MEDIUM;
    
    private BigDecimal budget;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
}