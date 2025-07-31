package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.modules.projects.entity.ProjectTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTeamMemberRepository extends JpaRepository<ProjectTeamMember, Long> {
    
    List<ProjectTeamMember> findByProjectId(Long projectId);
    
    List<ProjectTeamMember> findByUserId(Long userId);
    
    Optional<ProjectTeamMember> findByProjectIdAndUserId(Long projectId, Long userId);
    
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveTeamMembersByProjectId(@Param("projectId") Long projectId);
    
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.user.id = :userId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveProjectsByUserId(@Param("userId") Long userId);
    
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}