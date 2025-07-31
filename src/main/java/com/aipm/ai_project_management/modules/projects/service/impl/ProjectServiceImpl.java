package com.aipm.ai_project_management.modules.projects.service.impl;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.clients.entity.Client;
import com.aipm.ai_project_management.modules.clients.repository.ClientRepository;
import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone;
import com.aipm.ai_project_management.modules.projects.entity.ProjectTeamMember;
import com.aipm.ai_project_management.modules.projects.repository.ProjectMilestoneRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectTeamMemberRepository;
import com.aipm.ai_project_management.modules.projects.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectTeamMemberRepository teamMemberRepository;
    private final ProjectMilestoneRepository milestoneRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    
    @Override
    public ProjectDTO createProject(CreateProjectRequest request) {
        log.debug("Creating new project: {}", request.getName());
        
        Client client = clientRepository.findById(request.getClientId())
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));
        
        User manager = null;
        if (request.getManagerId() != null) {
            manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
        }
        
        Project project = Project.builder()
            .name(request.getName())
            .description(request.getDescription())
            .client(client)
            .manager(manager)
            .status(request.getStatus())
            .priority(request.getPriority())
            .budget(request.getBudget())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .build();
        
        project = projectRepository.save(project);
        log.info("Created project with id: {}", project.getId());
        
        return convertToDTO(project);
    }
    
    @Override
    public ProjectDTO updateProject(Long id, UpdateProjectRequest request) {
        log.debug("Updating project with id: {}", id);
        
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        
        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            project.setManager(manager);
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            project.setPriority(request.getPriority());
        }
        if (request.getBudget() != null) {
            project.setBudget(request.getBudget());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }
        if (request.getActualStartDate() != null) {
            project.setActualStartDate(request.getActualStartDate());
        }
        if (request.getActualEndDate() != null) {
            project.setActualEndDate(request.getActualEndDate());
        }
        if (request.getProgress() != null) {
            project.setProgress(request.getProgress());
        }
        
        project = projectRepository.save(project);
        log.info("Updated project with id: {}", project.getId());
        
        return convertToDTO(project);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return convertToDTO(project);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectsByClientId(Long clientId, Pageable pageable) {
        return projectRepository.findByClientId(clientId, pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectsByManagerId(Long managerId, Pageable pageable) {
        return projectRepository.findByManagerId(managerId, pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectsByStatus(ProjectStatus status, Pageable pageable) {
        return projectRepository.findByStatus(status, pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> searchProjects(String keyword, Pageable pageable) {
        return projectRepository.searchByKeyword(keyword, pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectsByTeamMemberId(Long userId, Pageable pageable) {
        return projectRepository.findProjectsByTeamMemberId(userId, pageable)
            .map(this::convertToDTO);
    }
    
    @Override
    public void deleteProject(Long id) {
        log.debug("Deleting project with id: {}", id);
        
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        
        projectRepository.deleteById(id);
        log.info("Deleted project with id: {}", id);
    }
    
    @Override
    public ProjectTeamMemberDTO addTeamMember(Long projectId, Long userId, String role) {
        log.debug("Adding team member {} to project {}", userId, projectId);
        
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        if (teamMemberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new IllegalArgumentException("User is already a team member of this project");
        }
        
        ProjectTeamMember teamMember = ProjectTeamMember.builder()
            .project(project)
            .user(user)
            .role(role)
            .build();
        
        teamMember = teamMemberRepository.save(teamMember);
        log.info("Added team member {} to project {}", userId, projectId);
        
        return convertToTeamMemberDTO(teamMember);
    }
    
    @Override
    public void removeTeamMember(Long projectId, Long userId) {
        log.debug("Removing team member {} from project {}", userId, projectId);
        
        ProjectTeamMember teamMember = teamMemberRepository.findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));
        
        teamMember.setLeftAt(LocalDateTime.now());
        teamMemberRepository.save(teamMember);
        
        log.info("Removed team member {} from project {}", userId, projectId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectTeamMemberDTO> getProjectTeamMembers(Long projectId) {
        List<ProjectTeamMember> teamMembers = teamMemberRepository.findActiveTeamMembersByProjectId(projectId);
        return teamMembers.stream()
            .map(this::convertToTeamMemberDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProjectMilestoneDTO createMilestone(Long projectId, ProjectMilestoneDTO milestoneDTO) {
        log.debug("Creating milestone for project: {}", projectId);
        
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        
        ProjectMilestone milestone = ProjectMilestone.builder()
            .project(project)
            .name(milestoneDTO.getName())
            .description(milestoneDTO.getDescription())
            .dueDate(milestoneDTO.getDueDate())
            .status(milestoneDTO.getStatus())
            .sortOrder(milestoneDTO.getSortOrder())
            .build();
        
        milestone = milestoneRepository.save(milestone);
        log.info("Created milestone with id: {}", milestone.getId());
        
        return convertToMilestoneDTO(milestone);
    }
    
    @Override
    public ProjectMilestoneDTO updateMilestone(Long milestoneId, ProjectMilestoneDTO milestoneDTO) {
        log.debug("Updating milestone with id: {}", milestoneId);
        
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
            .orElseThrow(() -> new ResourceNotFoundException("Milestone not found with id: " + milestoneId));
        
        milestone.setName(milestoneDTO.getName());
        milestone.setDescription(milestoneDTO.getDescription());
        milestone.setDueDate(milestoneDTO.getDueDate());
        milestone.setStatus(milestoneDTO.getStatus());
        milestone.setProgress(milestoneDTO.getProgress());
        milestone.setSortOrder(milestoneDTO.getSortOrder());
        milestone.setUpdatedAt(LocalDateTime.now());
        
        milestone = milestoneRepository.save(milestone);
        log.info("Updated milestone with id: {}", milestone.getId());
        
        return convertToMilestoneDTO(milestone);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectMilestoneDTO> getProjectMilestones(Long projectId) {
        List<ProjectMilestone> milestones = milestoneRepository.findByProjectIdOrderBySortOrder(projectId);
        return milestones.stream()
            .map(this::convertToMilestoneDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteMilestone(Long milestoneId) {
        log.debug("Deleting milestone with id: {}", milestoneId);
        
        if (!milestoneRepository.existsById(milestoneId)) {
            throw new ResourceNotFoundException("Milestone not found with id: " + milestoneId);
        }
        
        milestoneRepository.deleteById(milestoneId);
        log.info("Deleted milestone with id: {}", milestoneId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getProjectCountByStatus(ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getRecentProjects(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return projectRepository.findAll(pageable).getContent()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO.ProjectDTOBuilder builder = ProjectDTO.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .status(project.getStatus())
            .priority(project.getPriority())
            .progress(project.getProgress())
            .budget(project.getBudget())
            .spent(project.getSpent())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .aiHealthScore(project.getAiHealthScore())
            .aiRiskLevel(project.getAiRiskLevel())
            .createdAt(project.getCreatedAt())
            .updatedAt(project.getUpdatedAt());
        
        if (project.getClient() != null) {
            builder.client(ProjectDTO.ClientSummaryDTO.builder()
                .id(project.getClient().getId())
                .name(project.getClient().getName())
                .companyName(project.getClient().getName()) // Using name as company name
                .build());
        }
        
        if (project.getManager() != null) {
            builder.manager(ProjectDTO.UserSummaryDTO.builder()
                .id(project.getManager().getId())
                .firstName(project.getManager().getName()) // Using name as firstName
                .lastName("") // Empty since we only have name field
                .email(project.getManager().getEmail())
                .build());
        }
        
        return builder.build();
    }
    
    private ProjectTeamMemberDTO convertToTeamMemberDTO(ProjectTeamMember teamMember) {
        return ProjectTeamMemberDTO.builder()
            .id(teamMember.getId())
            .projectId(teamMember.getProject().getId())
            .userId(teamMember.getUser().getId())
            .firstName(teamMember.getUser().getName()) // Using name as firstName
            .lastName("") // Empty since we only have name field
            .email(teamMember.getUser().getEmail())
            .role(teamMember.getRole())
            .allocationPercentage(teamMember.getAllocationPercentage())
            .hourlyRate(teamMember.getHourlyRate())
            .joinedAt(teamMember.getJoinedAt())
            .leftAt(teamMember.getLeftAt())
            .build();
    }
    
    private ProjectMilestoneDTO convertToMilestoneDTO(ProjectMilestone milestone) {
        return ProjectMilestoneDTO.builder()
            .id(milestone.getId())
            .projectId(milestone.getProject().getId())
            .name(milestone.getName())
            .description(milestone.getDescription())
            .dueDate(milestone.getDueDate())
            .status(milestone.getStatus())
            .progress(milestone.getProgress())
            .sortOrder(milestone.getSortOrder())
            .createdAt(milestone.getCreatedAt())
            .updatedAt(milestone.getUpdatedAt())
            .build();
    }
}