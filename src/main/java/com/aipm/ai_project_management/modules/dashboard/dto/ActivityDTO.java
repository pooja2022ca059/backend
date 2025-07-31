package com.aipm.ai_project_management.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String type; // PROJECT_CREATED, TASK_COMPLETED, USER_JOINED, etc.
    private String description;
    private String entityType; // PROJECT, TASK, USER, CLIENT
    private Long entityId;
    private String entityName;
    private String actorName;
    private LocalDateTime timestamp;
    private String severity; // INFO, WARNING, ERROR
}