package com.aipm.ai_project_management.modules.projects.entity;

import com.aipm.ai_project_management.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_team_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(length = 100)
    private String role;
    
    @Column(name = "allocation_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal allocationPercentage = new BigDecimal("100.00");
    
    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;
    
    @Column(name = "joined_at")
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();
    
    @Column(name = "left_at")
    private LocalDateTime leftAt;
}