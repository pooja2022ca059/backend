package com.aipm.ai_project_management.modules.ai.dto;

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
public class AIInsightDTO {
    private String type; // RISK_PREDICTION, PERFORMANCE_ANALYSIS, RESOURCE_OPTIMIZATION, etc.
    private String title;
    private String description;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private BigDecimal confidence;
    private List<String> actionItems;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean isActive;
}