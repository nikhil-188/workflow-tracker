package com.workflowtracker.service;

import com.workflowtracker.dto.TaskDetailedDto;
import com.workflowtracker.dto.TaskSummaryDto;
import com.workflowtracker.dto.UserInTaskViewDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor //this creates the constructor for DI
public class TaskService
{
        private final TaskRepository taskRepository;
        private final UserValidationService userValidationService;

        // Create a task by manager and assign to employee
        public TaskDetailedDto createTask(
                        String title, String description, Priority priority, Long managerId, Long assignedToUserId,
                        LocalDate dueDate
        )
        {
            User manager = userValidationService.validateManager(managerId);
            User employee = userValidationService.validateEmployee(assignedToUserId);

            Task task = Task.builder()
                            .title(title)
                            .description(description)
                            .priority(priority)
                            .dueDate(dueDate)
                            .createdBy(manager)
                            .assignedTo(employee)
                            .build();

            // status + createdAt handled by @PrePersist
            Task savedTask = taskRepository.save(task);
            return taskDetailedDto(savedTask);
        }

        //Fetch tasks assigned to employee (Summary View)
        public List<TaskSummaryDto> getTasksForEmployee(Long employeeId)
        {
            User employee = userValidationService.validateEmployee(employeeId);

            return taskRepository.findByAssignedTo(employee)
                    .stream()
                    .map((task) -> taskSummaryDto(task))
                    .toList();
        }

        //Fetch tasks assigned to manager (Summary View)
        public List<TaskSummaryDto> getTasksCreatedByManager(Long managerId)
        {
            User manager = userValidationService.validateManager(managerId);

            return taskRepository.findByCreatedBy(manager)
                    .stream()
                    .map((task) -> taskSummaryDto(task))
                    .toList();
        }

        //TODO when login is introduced.... these methods of employee and manager will become one since we don't pass UserId in the URL

        // Get single task by employeeID (Detailed View)
        public TaskDetailedDto getDetailedTaskByEmployeeId(Long employeeId, Long taskId)
        {
            User employee = userValidationService.validateEmployee(employeeId);
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

            if(!task.getAssignedTo().getId().equals(employeeId))
            {
                throw new RuntimeException("Task is not assigned to you, hence you cannot access it");
            }
            return taskDetailedDto(task);
        }

        // Get single task by managerId (Detailed View)
        public TaskDetailedDto getDetailedTaskByManagerId(Long managerId, Long taskId)
        {
           User manager = userValidationService.validateManager(managerId);
           Task task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not found"));

           if(!task.getCreatedBy().getId().equals(managerId))
           {
               throw new RuntimeException("The task is not created by you, hence you cannot access it");
           }
           return taskDetailedDto(task);
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
