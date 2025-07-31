package com.aipm.ai_project_management.modules.dashboard.service.impl;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.clients.repository.ClientRepository;
import com.aipm.ai_project_management.modules.dashboard.dto.*;
import com.aipm.ai_project_management.modules.dashboard.service.DashboardService;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TimeTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TimeTrackingRepository timeTrackingRepository;
    
    @Override
    public AdminDashboardDTO getAdminDashboard() {
        log.debug("Generating admin dashboard");
        
        return AdminDashboardDTO.builder()
            .systemMetrics(buildSystemMetrics())
            .projectStats(buildProjectStats())
            .userStats(buildUserStats())
            .clientStats(buildClientStats())
            .taskStats(buildTaskStats())
            .recentActivities(buildRecentActivities(10))
            .performanceMetrics(buildPerformanceMetrics())
            .build();
    }
    
    @Override
    public ProjectManagerDashboardDTO getProjectManagerDashboard(Long managerId) {
        log.debug("Generating project manager dashboard for user: {}", managerId);
        
        return ProjectManagerDashboardDTO.builder()
            .projectOverview(buildProjectManagerOverview(managerId))
            .teamPerformance(buildTeamPerformance(managerId))
            .taskManagement(buildTaskManagement(managerId))
            .recentProjects(buildRecentProjects(managerId, 5))
            .upcomingDeadlines(buildUpcomingDeadlines(managerId, 10))
            .aiInsights(buildAIInsights(managerId, 5))
            .build();
    }
    
    @Override
    public TeamMemberDashboardDTO getTeamMemberDashboard(Long userId) {
        log.debug("Generating team member dashboard for user: {}", userId);
        
        return TeamMemberDashboardDTO.builder()
            .personalOverview(buildPersonalOverview(userId))
            .assignedTasks(buildAssignedTasks(userId, 10))
            .recentActivities(buildUserRecentActivities(userId, 10))
            .timeTracking(buildTimeTracking(userId))
            .personalPerformance(buildPersonalPerformance(userId))
            .build();
    }
    
    @Override
    public ClientDashboardDTO getClientDashboard(Long clientId) {
        log.debug("Generating client dashboard for client: {}", clientId);
        
        return ClientDashboardDTO.builder()
            .projectOverview(buildClientProjectOverview(clientId))
            .activeProjects(buildClientActiveProjects(clientId, 10))
            .recentUpdates(buildProjectUpdates(clientId, 10))
            .budgetSummary(buildBudgetSummary(clientId))
            .supportSummary(buildSupportSummary(clientId))
            .build();
    }
    
    private AdminDashboardDTO.SystemMetricsDTO buildSystemMetrics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActiveTrue();
        long totalClients = clientRepository.count();
        long totalProjects = projectRepository.count();
        long totalTasks = taskRepository.count();
        
        return AdminDashboardDTO.SystemMetricsDTO.builder()
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .totalClients(totalClients)
            .totalProjects(totalProjects)
            .totalTasks(totalTasks)
            .systemHealthScore(BigDecimal.valueOf(95.5)) // Mock value
            .build();
    }
    
    private AdminDashboardDTO.ProjectStatsDTO buildProjectStats() {
        long totalProjects = projectRepository.count();
        long activeProjects = projectRepository.countByStatus(ProjectStatus.IN_PROGRESS);
        long completedProjects = projectRepository.countByStatus(ProjectStatus.COMPLETED);
        long overdueProjects = 0; // Would need custom query for overdue logic
        
        return AdminDashboardDTO.ProjectStatsDTO.builder()
            .totalProjects(totalProjects)
            .activeProjects(activeProjects)
            .completedProjects(completedProjects)
            .overdueProjects(overdueProjects)
            .averageProgress(BigDecimal.valueOf(65.5)) // Mock value
            .totalBudget(BigDecimal.valueOf(1000000))
            .totalSpent(BigDecimal.valueOf(650000))
            .build();
    }
    
    private AdminDashboardDTO.UserStatsDTO buildUserStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActiveTrue();
        long admins = userRepository.countByRole(UserRole.ADMIN);
        long projectManagers = userRepository.countByRole(UserRole.PROJECT_MANAGER);
        long teamMembers = userRepository.countByRole(UserRole.TEAM_MEMBER);
        long clients = userRepository.countByRole(UserRole.CLIENT);
        
        return AdminDashboardDTO.UserStatsDTO.builder()
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .admins(admins)
            .projectManagers(projectManagers)
            .teamMembers(teamMembers)
            .clients(clients)
            .build();
    }
    
    private AdminDashboardDTO.ClientStatsDTO buildClientStats() {
        long totalClients = clientRepository.count();
        // Mock values for demonstration
        return AdminDashboardDTO.ClientStatsDTO.builder()
            .totalClients(totalClients)
            .activeClients(totalClients)
            .newClientsThisMonth(5L)
            .averageProjectsPerClient(BigDecimal.valueOf(2.5))
            .build();
    }
    
    private AdminDashboardDTO.TaskStatsDTO buildTaskStats() {
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus(TaskStatus.DONE);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        // Mock overdue tasks calculation
        long overdueTasks = 0;
        
        return AdminDashboardDTO.TaskStatsDTO.builder()
            .totalTasks(totalTasks)
            .completedTasks(completedTasks)
            .inProgressTasks(inProgressTasks)
            .overdueTasks(overdueTasks)
            .averageCompletionTime(BigDecimal.valueOf(3.5))
            .build();
    }
    
    private List<ActivityDTO> buildRecentActivities(int limit) {
        // Mock implementation - in real app, would query activity log
        return new ArrayList<>();
    }
    
    private AdminDashboardDTO.PerformanceMetricsDTO buildPerformanceMetrics() {
        return AdminDashboardDTO.PerformanceMetricsDTO.builder()
            .projectSuccessRate(BigDecimal.valueOf(92.5))
            .onTimeDeliveryRate(BigDecimal.valueOf(88.0))
            .budgetAccuracyRate(BigDecimal.valueOf(95.0))
            .teamProductivity(BigDecimal.valueOf(87.5))
            .build();
    }
    
    private ProjectManagerDashboardDTO.ProjectOverviewDTO buildProjectManagerOverview(Long managerId) {
        List<Project> managerProjects = projectRepository.findByManagerIdAndStatus(managerId, ProjectStatus.IN_PROGRESS);
        
        long totalProjects = managerProjects.size();
        long activeProjects = managerProjects.stream()
            .mapToLong(p -> p.getStatus() == ProjectStatus.IN_PROGRESS ? 1 : 0)
            .sum();
        
        return ProjectManagerDashboardDTO.ProjectOverviewDTO.builder()
            .totalProjects(totalProjects)
            .activeProjects(activeProjects)
            .completedProjects(0L) // Mock
            .overdueProjects(0L) // Mock
            .averageProgress(BigDecimal.valueOf(65.0))
            .totalBudget(BigDecimal.valueOf(500000))
            .totalSpent(BigDecimal.valueOf(325000))
            .averageHealthScore(BigDecimal.valueOf(8.5))
            .build();
    }
    
    private ProjectManagerDashboardDTO.TeamPerformanceDTO buildTeamPerformance(Long managerId) {
        return ProjectManagerDashboardDTO.TeamPerformanceDTO.builder()
            .totalTeamMembers(15L)
            .averageProductivity(BigDecimal.valueOf(85.5))
            .tasksCompleted(125L)
            .averageTaskCompletionTime(BigDecimal.valueOf(2.5))
            .topPerformers(new ArrayList<>()) // Mock
            .build();
    }
    
    private ProjectManagerDashboardDTO.TaskManagementDTO buildTaskManagement(Long managerId) {
        return ProjectManagerDashboardDTO.TaskManagementDTO.builder()
            .totalTasks(250L)
            .completedTasks(175L)
            .inProgressTasks(50L)
            .overdueTasks(25L)
            .tasksAssignedToday(5L)
            .completionRate(BigDecimal.valueOf(70.0))
            .build();
    }
    
    private List<ProjectManagerDashboardDTO.ProjectSummaryDTO> buildRecentProjects(Long managerId, int limit) {
        List<Project> projects = projectRepository.findByManagerId(
            managerId, 
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();
        
        return projects.stream()
            .map(project -> ProjectManagerDashboardDTO.ProjectSummaryDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .status(project.getStatus().name())
                .progress(project.getProgress())
                .deadline(project.getEndDate() != null ? project.getEndDate().atStartOfDay() : null)
                .clientName(project.getClient() != null ? project.getClient().getName() : "Unknown")
                .build())
            .collect(Collectors.toList());
    }
    
    private List<ProjectManagerDashboardDTO.UpcomingDeadlineDTO> buildUpcomingDeadlines(Long managerId, int limit) {
        // Mock implementation
        return new ArrayList<>();
    }
    
    private List<ProjectManagerDashboardDTO.AIInsightDTO> buildAIInsights(Long managerId, int limit) {
        // Mock implementation
        return new ArrayList<>();
    }
    
    private TeamMemberDashboardDTO.PersonalOverviewDTO buildPersonalOverview(Long userId) {
        List<Task> userTasks = taskRepository.findByAssigneeId(userId);
        
        long totalTasks = userTasks.size();
        long completedTasks = userTasks.stream()
            .mapToLong(t -> t.getStatus() == TaskStatus.DONE ? 1 : 0)
            .sum();
        long inProgressTasks = userTasks.stream()
            .mapToLong(t -> t.getStatus() == TaskStatus.IN_PROGRESS ? 1 : 0)
            .sum();
        
        BigDecimal completionRate = totalTasks > 0 ? 
            BigDecimal.valueOf(completedTasks).divide(BigDecimal.valueOf(totalTasks), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
        
        return TeamMemberDashboardDTO.PersonalOverviewDTO.builder()
            .totalTasks(totalTasks)
            .completedTasks(completedTasks)
            .inProgressTasks(inProgressTasks)
            .overdueTasks(0L) // Mock
            .completionRate(completionRate)
            .activeProjects(3L) // Mock
            .build();
    }
    
    private List<TeamMemberDashboardDTO.AssignedTaskDTO> buildAssignedTasks(Long userId, int limit) {
        List<Task> tasks = taskRepository.findByAssigneeId(
            userId,
            PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "dueDate"))
        ).getContent();
        
        return tasks.stream()
            .map(task -> TeamMemberDashboardDTO.AssignedTaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .projectName("Project " + task.getProjectId()) // Mock since we only have projectId
                .dueDate(task.getDueDate())
                .progress(task.getProgress() != null ? BigDecimal.valueOf(task.getProgress()) : BigDecimal.ZERO)
                .estimatedHours(task.getEstimatedHours())
                .loggedHours(task.getLoggedHours())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<ActivityDTO> buildUserRecentActivities(Long userId, int limit) {
        // Mock implementation
        return new ArrayList<>();
    }
    
    private TeamMemberDashboardDTO.TimeTrackingDTO buildTimeTracking(Long userId) {
        // Mock implementation
        return TeamMemberDashboardDTO.TimeTrackingDTO.builder()
            .totalHoursToday(8.0)
            .totalHoursThisWeek(40.0)
            .totalHoursThisMonth(160.0)
            .averageHoursPerDay(8.0)
            .recentTimeEntries(new ArrayList<>())
            .build();
    }
    
    private TeamMemberDashboardDTO.PersonalPerformanceDTO buildPersonalPerformance(Long userId) {
        return TeamMemberDashboardDTO.PersonalPerformanceDTO.builder()
            .productivityScore(BigDecimal.valueOf(85.5))
            .tasksCompletedThisMonth(25L)
            .averageTaskCompletionTime(BigDecimal.valueOf(2.5))
            .onTimeDeliveryRate(BigDecimal.valueOf(92.0))
            .build();
    }
    
    private ClientDashboardDTO.ClientProjectOverviewDTO buildClientProjectOverview(Long clientId) {
        List<Project> clientProjects = projectRepository.findByClientIdAndStatus(clientId, ProjectStatus.IN_PROGRESS);
        
        return ClientDashboardDTO.ClientProjectOverviewDTO.builder()
            .totalProjects((long) clientProjects.size())
            .activeProjects((long) clientProjects.size())
            .completedProjects(0L) // Mock
            .onHoldProjects(0L) // Mock
            .overallProgress(BigDecimal.valueOf(65.0))
            .averageHealthScore(BigDecimal.valueOf(8.5))
            .build();
    }
    
    private List<ClientDashboardDTO.ClientProjectDTO> buildClientActiveProjects(Long clientId, int limit) {
        List<Project> projects = projectRepository.findByClientId(
            clientId,
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();
        
        return projects.stream()
            .map(project -> ClientDashboardDTO.ClientProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .status(project.getStatus().name())
                .progress(project.getProgress())
                .startDate(project.getStartDate() != null ? project.getStartDate().atStartOfDay() : null)
                .expectedEndDate(project.getEndDate() != null ? project.getEndDate().atStartOfDay() : null)
                .managerName(project.getManager() != null ? project.getManager().getName() : "Not Assigned")
                .budget(project.getBudget())
                .spent(project.getSpent())
                .healthScore(project.getAiHealthScore())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<ClientDashboardDTO.ProjectUpdateDTO> buildProjectUpdates(Long clientId, int limit) {
        // Mock implementation
        return new ArrayList<>();
    }
    
    private ClientDashboardDTO.BudgetSummaryDTO buildBudgetSummary(Long clientId) {
        List<Project> projects = projectRepository.findByClientIdAndStatus(clientId, ProjectStatus.IN_PROGRESS);
        
        BigDecimal totalBudget = projects.stream()
            .map(Project::getBudget)
            .filter(budget -> budget != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalSpent = projects.stream()
            .map(Project::getSpent)
            .filter(spent -> spent != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return ClientDashboardDTO.BudgetSummaryDTO.builder()
            .totalBudget(totalBudget)
            .totalSpent(totalSpent)
            .remainingBudget(totalBudget.subtract(totalSpent))
            .budgetUtilization(totalBudget.compareTo(BigDecimal.ZERO) > 0 ? 
                totalSpent.divide(totalBudget, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) : 
                BigDecimal.ZERO)
            .projectBudgets(new ArrayList<>())
            .build();
    }
    
    private ClientDashboardDTO.SupportSummaryDTO buildSupportSummary(Long clientId) {
        return ClientDashboardDTO.SupportSummaryDTO.builder()
            .openTickets(2L)
            .closedTickets(15L)
            .pendingApprovals(1L)
            .lastCommunication(LocalDateTime.now().minusDays(2))
            .projectManagerContact("manager@example.com")
            .build();
    }
}