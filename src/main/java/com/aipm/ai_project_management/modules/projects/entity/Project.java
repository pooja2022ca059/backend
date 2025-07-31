package com.aipm.ai_project_management.modules.projects.entity;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.clients.entity.Client;
import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PLANNING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectPriority priority = ProjectPriority.MEDIUM;
    
    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal progress = BigDecimal.ZERO;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal budget;
    
    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal spent = BigDecimal.ZERO;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;
    
    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;
    
    @Column(name = "ai_health_score", precision = 3, scale = 1)
    private BigDecimal aiHealthScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_risk_level")
    private ProjectPriority aiRiskLevel;
    
    // Team members relationship
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectTeamMember> teamMembers;
    
    // Milestones relationship
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectMilestone> milestones;
}