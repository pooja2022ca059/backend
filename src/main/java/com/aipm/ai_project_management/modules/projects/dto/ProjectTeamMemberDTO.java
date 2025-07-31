package com.aipm.ai_project_management.modules.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamMemberDTO {
    private Long id;
    private Long projectId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private BigDecimal allocationPercentage;
    private BigDecimal hourlyRate;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
}