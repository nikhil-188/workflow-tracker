package com.workflowtracker.service;

import com.workflowtracker.dto.TaskDetailedDto;
import com.workflowtracker.dto.TaskSummaryDto;
import com.workflowtracker.dto.UserInTaskViewDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.repository.TaskRepository;
import com.workflowtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
//TODO task status should be changed
@Service
@RequiredArgsConstructor //this creates the constructor for DI
public class TaskService
{
        private final TaskRepository taskRepository;
        private final UserRepository userRepository;
        private final CurrentUserService currentUserService;

        // Create a task by manager and assign to employee
        public TaskDetailedDto createTask(
                        String title, String description, Priority priority, Long assignedToUserId,
                        LocalDate dueDate
        )
        {
            User currentUser = currentUserService.getCurrentUser();

            if(currentUser.getRole()!=Role.MANAGER)
            {
                throw new RuntimeException("Only Managers can create tasks");
            }

            User employee = userRepository.findById(assignedToUserId).orElseThrow(()-> new RuntimeException("Employee not found"));
            if(employee.getRole()!=Role.EMPLOYEE)
            {
                throw new RuntimeException("Tasks must be assigned to only Employees");
            }

            Task task = Task.builder()
                            .title(title)
                            .description(description)
                            .priority(priority)
                            .dueDate(dueDate)
                            .createdBy(currentUser)
                            .assignedTo(employee)
                            .build();

            // status + createdAt handled by @PrePersist
            Task savedTask = taskRepository.save(task);
            return taskDetailedDto(savedTask);
        }

        //Combined method for both Manager and Employee to view tasks (Summary view)
        public List<TaskSummaryDto> getMyTasksSummary()
        {
            User currentUser = currentUserService.getCurrentUser();

            if(currentUser.getRole()==Role.MANAGER)
            {
                return taskRepository.findByCreatedBy(currentUser)
                        .stream()
                        .map((task) -> taskSummaryDto(task))
                        .toList();
            }
            if(currentUser.getRole()==Role.EMPLOYEE)
            {
                return taskRepository.findByAssignedTo(currentUser)
                        .stream()
                        .map((task) -> taskSummaryDto(task))
                        .toList();
            }
            throw new RuntimeException("Invalid Role");
        }

        //Combined method for both Manager and Employee to view tasks (Summary view)
        public TaskDetailedDto getMyTasksDetailed(Long taskId)
        {
            User currentUser = currentUserService.getCurrentUser();
            Task task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not found"));
            if(currentUser.getRole()==Role.MANAGER && task.getCreatedBy().getId().equals(currentUser.getId()))
            {
                return taskDetailedDto(task);
            }
            if(currentUser.getRole()==Role.EMPLOYEE && task.getAssignedTo().getId().equals(currentUser.getId()))
            {
                return taskDetailedDto(task);
            }
            throw new RuntimeException("You are not allowed to access this task");
        }

        //TODO manager should be able to delete the the tasks

        //Mapping the task object to taskSummaryDto object (Summary view)
        private TaskSummaryDto taskSummaryDto(Task task) {
            return TaskSummaryDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .taskStatus(task.getStatus())
                    .deadline(task.getDueDate())
                    .createdAt(task.getCreatedAt())
                    .assignedTo(mapUser(task.getAssignedTo()))
                    .build();
        }

        // Mapping the task object to taskDetailedDto object (Detailed view)
        private TaskDetailedDto taskDetailedDto(Task task)
        {
                return new TaskDetailedDto(
                                task.getId(),
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus(),
                                task.getPriority(),
                                task.getDueDate(),
                                task.getCreatedAt(),
                                mapUser(task.getCreatedBy()),
                                mapUser(task.getAssignedTo()));
        }

        private UserInTaskViewDto mapUser(User user)
        {
                if (user == null)
                        return null;
                return new UserInTaskViewDto(
                                user.getId(),
                                user.getName()
                );
        }
}
