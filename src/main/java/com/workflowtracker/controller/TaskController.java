package com.workflowtracker.controller;

import com.workflowtracker.dto.TaskResponseDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // Manager creates tasks
    @PostMapping
    public TaskResponseDto createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Priority priority,
            @RequestParam Long assignedToUserId,
            @RequestParam String dueDate) {
        // Temporary hardcoded manager (later replaced by spring security)
        Long managerId = 1L; // assuming manager id = 1

        LocalDate parsedDueDate = LocalDate.parse(dueDate);

        return taskService.createTask(title, description, priority, 1L, assignedToUserId, parsedDueDate);
    }

    @Autowired
    private com.workflowtracker.service.TaskSummaryService taskSummaryService;

    // Employee views tasks assigned to him (Summary only)
    @GetMapping("/employee/{employeeId}")
    public List<com.workflowtracker.dto.TaskSummaryDto> getTasksForEmployee(@PathVariable Long employeeId) {
        return taskSummaryService.getTasksForEmployee(employeeId);
    }

    // Manager views tasks he created (Summary only)
    @GetMapping("/manager/{managerId}")
    public List<com.workflowtracker.dto.TaskSummaryDto> getTaskCreatedByManager(@PathVariable Long managerId) {
        return taskSummaryService.getTasksCreatedByManager(managerId);
    }

    // NEW: Get full details for a single task
    @GetMapping("/{taskId}")
    public TaskResponseDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }
    // TODO if manager is logged in give him Task id, name, priority, assigned
    // to..... once it is clicked in the task then give all the details
}
