package com.workflowtracker.controller;

import com.workflowtracker.dto.CreateTaskRequest;
import com.workflowtracker.dto.TaskResponseDto;
import com.workflowtracker.dto.TaskSummaryDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor //this creates the constructor for DI
public class TaskController
{
    private final TaskService taskService;

    // Manager creates tasks
    @PostMapping("{managerId}")
    public TaskResponseDto createTask(
            @RequestBody CreateTaskRequest request,
            @PathVariable Long managerId)
    {
        return taskService.createTask(request.getTitle(), request.getDescription(), request.getPriority(), managerId, request.getAssignedToUserId(), request.getDueDate());
    }

    @Autowired
    private com.workflowtracker.service.TaskSummaryService taskSummaryService;

    // Employee views tasks assigned to him (Summary only)
    @GetMapping("/employee/{employeeId}")
    public List<TaskSummaryDto> getTasksForEmployee(@PathVariable Long employeeId) {
        return taskSummaryService.getTasksForEmployee(employeeId);
    }

    // Manager views tasks he created (Summary only)
    @GetMapping("/manager/{managerId}")
    public List<TaskSummaryDto> getTaskCreatedByManager(@PathVariable Long managerId) {
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
