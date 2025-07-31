package com.aipm.ai_project_management.modules.dashboard.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.dashboard.dto.*;
import com.aipm.ai_project_management.modules.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard API for different user roles")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/admin")
    @Operation(summary = "Get admin dashboard with system-wide metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> getAdminDashboard() {
        AdminDashboardDTO dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/pm")
    @Operation(summary = "Get project manager dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectManagerDashboardDTO>> getProjectManagerDashboard(
            @RequestParam(required = false) Long managerId) {
        
        // If managerId is not provided, use current user's ID (would get from security context)
        if (managerId == null) {
            // In real implementation, get from SecurityContextHolder
            managerId = 1L; // Mock value
        }
        
        ProjectManagerDashboardDTO dashboard = dashboardService.getProjectManagerDashboard(managerId);
        return ResponseEntity.ok(ApiResponse.success("Project manager dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/team")
    @Operation(summary = "Get team member dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TeamMemberDashboardDTO>> getTeamMemberDashboard(
            @RequestParam(required = false) Long userId) {
        
        // If userId is not provided, use current user's ID (would get from security context)
        if (userId == null) {
            // In real implementation, get from SecurityContextHolder
            userId = 1L; // Mock value
        }
        
        TeamMemberDashboardDTO dashboard = dashboardService.getTeamMemberDashboard(userId);
        return ResponseEntity.ok(ApiResponse.success("Team member dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/client")
    @Operation(summary = "Get client dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('CLIENT')")
    public ResponseEntity<ApiResponse<ClientDashboardDTO>> getClientDashboard(
            @RequestParam(required = false) Long clientId) {
        
        // If clientId is not provided, use current user's client ID (would get from security context)
        if (clientId == null) {
            // In real implementation, get from SecurityContextHolder
            clientId = 1L; // Mock value
        }
        
        ClientDashboardDTO dashboard = dashboardService.getClientDashboard(clientId);
        return ResponseEntity.ok(ApiResponse.success("Client dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/pm/{managerId}")
    @Operation(summary = "Get project manager dashboard by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @userSecurityService.isSameUser(#managerId, authentication.name)")
    public ResponseEntity<ApiResponse<ProjectManagerDashboardDTO>> getProjectManagerDashboardById(
            @PathVariable Long managerId) {
        
        ProjectManagerDashboardDTO dashboard = dashboardService.getProjectManagerDashboard(managerId);
        return ResponseEntity.ok(ApiResponse.success("Project manager dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/team/{userId}")
    @Operation(summary = "Get team member dashboard by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @userSecurityService.isSameUser(#userId, authentication.name)")
    public ResponseEntity<ApiResponse<TeamMemberDashboardDTO>> getTeamMemberDashboardById(
            @PathVariable Long userId) {
        
        TeamMemberDashboardDTO dashboard = dashboardService.getTeamMemberDashboard(userId);
        return ResponseEntity.ok(ApiResponse.success("Team member dashboard retrieved successfully", dashboard));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get client dashboard by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or @clientSecurityService.canAccessClient(#clientId, authentication.name)")
    public ResponseEntity<ApiResponse<ClientDashboardDTO>> getClientDashboardById(
            @PathVariable Long clientId) {
        
        ClientDashboardDTO dashboard = dashboardService.getClientDashboard(clientId);
        return ResponseEntity.ok(ApiResponse.success("Client dashboard retrieved successfully", dashboard));
    }
}