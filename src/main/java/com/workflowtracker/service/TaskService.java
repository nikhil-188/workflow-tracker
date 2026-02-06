package com.workflowtracker.service;

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
    public Task createTask(
            String title, String description, Priority priority, Long managerId, Long assignedtoUserId, LocalDate dueDate)
    {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        User employee = userRepository.findById(assignedtoUserId)
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
                .createdBy(manager)
                .assignedTo(employee)
                .dueDate(dueDate)
                .build();

        //status + createdAt handled by @PrePersist
        return taskRepository.save(task);
    }

    //Get tasks assigned to employee
    public List<Task> getTasksForEmployee(User employee)
    {
        return taskRepository.findByAssignedTo(employee);
    }

    //Get tasks created by manager
    public List<Task> getTasksCreatedByManager(User manager)
    {
        return taskRepository.findByCreatedBy(manager);
    }
}
