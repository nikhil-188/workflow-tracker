package com.workflowtracker.service;

import com.workflowtracker.dto.TaskResponseDto;
import com.workflowtracker.dto.UserResponseDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.repository.TaskRepository;
import com.workflowtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService
{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    //Create a task by manager and assign to employee
    public TaskResponseDto createTask(
            String title, String description, Priority priority, Long managerId, Long assignedToUserId, LocalDate dueDate)
    {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        User employee = userRepository.findById(assignedToUserId)
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));

        //Business rule task can be assigned to only employee
        if(employee.getRole()!=Role.EMPLOYEE)
        {
            throw new RuntimeException("Task can be assigned to only employee");
        }

        Task task = Task.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .dueDate(dueDate)
                .createdBy(manager)
                .assignedTo(employee)
                .build();

        //status + createdAt handled by @PrePersist
        Task savedTask = taskRepository.save(task);

        return mapToDto(savedTask);
    }

    //Get tasks assigned to employee
    public List<TaskResponseDto> getTasksForEmployee(Long employeeId)
    {
        User employee = userRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        return taskRepository.findByAssignedTo(employee)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    //Get tasks created by manager
    public List<TaskResponseDto> getTasksCreatedByManager(Long managerId)
    {
        User manager = userRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));

        return taskRepository.findByCreatedBy(manager)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    //Centralized mapped logic
    private TaskResponseDto mapToDto(Task task)
    {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                mapUser(task.getCreatedBy()),
                mapUser(task.getAssignedTo())
        );
    }

    private UserResponseDto mapUser(User user)
    {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
