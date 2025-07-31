package com.aipm.ai_project_management.modules.projects.controller;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.PageResponse;
import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.projects.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management API")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @PostMapping
    @Operation(summary = "Create a new project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectDTO project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Project created successfully", project));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canAccessProject(#id, authentication.name)")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProject(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success("Project retrieved successfully", project));
    }
    
    @GetMapping
    @Operation(summary = "Get all projects with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProjectDTO> projects = projectService.getAllProjects(pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", response));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get projects by client ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @clientSecurityService.canAccessClient(#clientId, authentication.name)")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> getProjectsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectDTO> projects = projectService.getProjectsByClientId(clientId, pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Client projects retrieved successfully", response));
    }
    
    @GetMapping("/manager/{managerId}")
    @Operation(summary = "Get projects by manager ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @userSecurityService.isSameUser(#managerId, authentication.name)")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> getProjectsByManager(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectDTO> projects = projectService.getProjectsByManagerId(managerId, pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Manager projects retrieved successfully", response));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> getProjectsByStatus(
            @PathVariable ProjectStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectDTO> projects = projectService.getProjectsByStatus(status, pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Projects by status retrieved successfully", response));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search projects by keyword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> searchProjects(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectDTO> projects = projectService.searchProjects(keyword, pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Project search completed successfully", response));
    }
    
    @GetMapping("/team-member/{userId}")
    @Operation(summary = "Get projects by team member ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @userSecurityService.isSameUser(#userId, authentication.name)")
    public ResponseEntity<ApiResponse<PageResponse<ProjectDTO>>> getProjectsByTeamMember(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectDTO> projects = projectService.getProjectsByTeamMemberId(userId, pageable);
        PageResponse<ProjectDTO> response = PageResponse.of(projects);
        
        return ResponseEntity.ok(ApiResponse.success("Team member projects retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canModifyProject(#id, authentication.name)")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        
        ProjectDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", project));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", null));
    }
    
    // Team Member Management
    @PostMapping("/{projectId}/team-members")
    @Operation(summary = "Add team member to project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canModifyProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<ProjectTeamMemberDTO>> addTeamMember(
            @PathVariable Long projectId,
            @RequestParam Long userId,
            @RequestParam String role) {
        
        ProjectTeamMemberDTO teamMember = projectService.addTeamMember(projectId, userId, role);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Team member added successfully", teamMember));
    }
    
    @DeleteMapping("/{projectId}/team-members/{userId}")
    @Operation(summary = "Remove team member from project")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canModifyProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<Void>> removeTeamMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        
        projectService.removeTeamMember(projectId, userId);
        return ResponseEntity.ok(ApiResponse.success("Team member removed successfully", null));
    }
    
    @GetMapping("/{projectId}/team-members")
    @Operation(summary = "Get project team members")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canAccessProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<List<ProjectTeamMemberDTO>>> getTeamMembers(@PathVariable Long projectId) {
        List<ProjectTeamMemberDTO> teamMembers = projectService.getProjectTeamMembers(projectId);
        return ResponseEntity.ok(ApiResponse.success("Team members retrieved successfully", teamMembers));
    }
    
    // Milestone Management
    @PostMapping("/{projectId}/milestones")
    @Operation(summary = "Create project milestone")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canModifyProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<ProjectMilestoneDTO>> createMilestone(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMilestoneDTO milestoneDTO) {
        
        ProjectMilestoneDTO milestone = projectService.createMilestone(projectId, milestoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Milestone created successfully", milestone));
    }
    
    @PutMapping("/milestones/{milestoneId}")
    @Operation(summary = "Update project milestone")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectMilestoneDTO>> updateMilestone(
            @PathVariable Long milestoneId,
            @Valid @RequestBody ProjectMilestoneDTO milestoneDTO) {
        
        ProjectMilestoneDTO milestone = projectService.updateMilestone(milestoneId, milestoneDTO);
        return ResponseEntity.ok(ApiResponse.success("Milestone updated successfully", milestone));
    }
    
    @GetMapping("/{projectId}/milestones")
    @Operation(summary = "Get project milestones")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @projectSecurityService.canAccessProject(#projectId, authentication.name)")
    public ResponseEntity<ApiResponse<List<ProjectMilestoneDTO>>> getMilestones(@PathVariable Long projectId) {
        List<ProjectMilestoneDTO> milestones = projectService.getProjectMilestones(projectId);
        return ResponseEntity.ok(ApiResponse.success("Milestones retrieved successfully", milestones));
    }
    
    @DeleteMapping("/milestones/{milestoneId}")
    @Operation(summary = "Delete project milestone")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteMilestone(@PathVariable Long milestoneId) {
        projectService.deleteMilestone(milestoneId);
        return ResponseEntity.ok(ApiResponse.success("Milestone deleted successfully", null));
    }
}