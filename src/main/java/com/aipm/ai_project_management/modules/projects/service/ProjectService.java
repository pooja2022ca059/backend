package com.aipm.ai_project_management.modules.projects.service;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.modules.projects.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    
    ProjectDTO createProject(CreateProjectRequest request);
    
    ProjectDTO updateProject(Long id, UpdateProjectRequest request);
    
    ProjectDTO getProjectById(Long id);
    
    Page<ProjectDTO> getAllProjects(Pageable pageable);
    
    Page<ProjectDTO> getProjectsByClientId(Long clientId, Pageable pageable);
    
    Page<ProjectDTO> getProjectsByManagerId(Long managerId, Pageable pageable);
    
    Page<ProjectDTO> getProjectsByStatus(ProjectStatus status, Pageable pageable);
    
    Page<ProjectDTO> searchProjects(String keyword, Pageable pageable);
    
    Page<ProjectDTO> getProjectsByTeamMemberId(Long userId, Pageable pageable);
    
    void deleteProject(Long id);
    
    // Team member management
    ProjectTeamMemberDTO addTeamMember(Long projectId, Long userId, String role);
    
    void removeTeamMember(Long projectId, Long userId);
    
    List<ProjectTeamMemberDTO> getProjectTeamMembers(Long projectId);
    
    // Milestone management
    ProjectMilestoneDTO createMilestone(Long projectId, ProjectMilestoneDTO milestoneDTO);
    
    ProjectMilestoneDTO updateMilestone(Long milestoneId, ProjectMilestoneDTO milestoneDTO);
    
    List<ProjectMilestoneDTO> getProjectMilestones(Long projectId);
    
    void deleteMilestone(Long milestoneId);
    
    // Dashboard statistics
    long getProjectCountByStatus(ProjectStatus status);
    
    List<ProjectDTO> getRecentProjects(int limit);
}