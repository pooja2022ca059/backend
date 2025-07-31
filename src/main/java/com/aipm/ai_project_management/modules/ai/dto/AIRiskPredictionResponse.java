package com.aipm.ai_project_management.modules.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIRiskPredictionResponse {
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private BigDecimal riskScore; // 0-100
    private BigDecimal confidence; // 0-100
    private List<RiskFactor> riskFactors;
    private List<String> recommendations;
    private PredictedOutcome predictedOutcome;
    private LocalDateTime analysisTimestamp;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskFactor {
        private String category; // BUDGET, TIMELINE, TEAM, SCOPE, etc.
        private String description;
        private BigDecimal impact; // 0-10
        private BigDecimal probability; // 0-100
        private String mitigation;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictedOutcome {
        private LocalDateTime predictedCompletionDate;
        private BigDecimal predictedBudgetOverrun;
        private BigDecimal qualityScore;
        private Map<String, Object> metrics;
    }
}