package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Page<Project> findByClientId(Long clientId, Pageable pageable);
    
    Page<Project> findByManagerId(Long managerId, Pageable pageable);
    
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.client.id = :clientId AND p.status = :status")
    List<Project> findByClientIdAndStatus(@Param("clientId") Long clientId, @Param("status") ProjectStatus status);
    
    @Query("SELECT p FROM Project p WHERE p.manager.id = :managerId AND p.status = :status")
    List<Project> findByManagerIdAndStatus(@Param("managerId") Long managerId, @Param("status") ProjectStatus status);
    
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.client LEFT JOIN FETCH p.manager WHERE p.id = :id")
    Optional<Project> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.teamMembers tm LEFT JOIN FETCH tm.user WHERE p.id = :id")
    Optional<Project> findByIdWithTeamMembers(@Param("id") Long id);
    
    @Query("SELECT p FROM Project p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Project> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    long countByStatus(@Param("status") ProjectStatus status);
    
    @Query("SELECT p FROM Project p JOIN p.teamMembers tm WHERE tm.user.id = :userId")
    Page<Project> findProjectsByTeamMemberId(@Param("userId") Long userId, Pageable pageable);
}