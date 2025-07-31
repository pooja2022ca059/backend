package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMilestoneDTO {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private LocalDate dueDate;
    private MilestoneStatus status;
    private BigDecimal progress;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}