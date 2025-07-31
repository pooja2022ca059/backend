package com.aipm.ai_project_management.modules.dashboard.service;

import com.aipm.ai_project_management.modules.dashboard.dto.*;

public interface DashboardService {
    
    /**
     * Get admin dashboard with system-wide metrics
     */
    AdminDashboardDTO getAdminDashboard();
    
    /**
     * Get project manager dashboard for a specific manager
     */
    ProjectManagerDashboardDTO getProjectManagerDashboard(Long managerId);
    
    /**
     * Get team member dashboard for a specific user
     */
    TeamMemberDashboardDTO getTeamMemberDashboard(Long userId);
    
    /**
     * Get client dashboard for a specific client
     */
    ClientDashboardDTO getClientDashboard(Long clientId);
}