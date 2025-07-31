package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone;
import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone.MilestoneStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {
    
    List<ProjectMilestone> findByProjectIdOrderBySortOrder(Long projectId);
    
    List<ProjectMilestone> findByProjectIdAndStatus(Long projectId, MilestoneStatus status);
    
    @Query("SELECT pm FROM ProjectMilestone pm WHERE pm.project.id = :projectId AND pm.dueDate BETWEEN :startDate AND :endDate")
    List<ProjectMilestone> findByProjectIdAndDueDateBetween(
        @Param("projectId") Long projectId, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT pm FROM ProjectMilestone pm WHERE pm.dueDate < :date AND pm.status != 'COMPLETED'")
    List<ProjectMilestone> findOverdueMilestones(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(pm) FROM ProjectMilestone pm WHERE pm.project.id = :projectId AND pm.status = :status")
    long countByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") MilestoneStatus status);
}