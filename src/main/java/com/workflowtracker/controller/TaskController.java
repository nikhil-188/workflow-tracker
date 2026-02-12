package com.workflowtracker.controller;

import com.workflowtracker.dto.CreateTaskRequest;
import com.workflowtracker.dto.TaskDetailedDto;
import com.workflowtracker.dto.TaskSummaryDto;
import com.workflowtracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor //this creates the constructor for DI
public class TaskController
{
    private final TaskService taskService;

    // Manager creates tasks
    @PostMapping("{managerId}")
    public TaskDetailedDto createTask(
            @RequestBody CreateTaskRequest request,
            @PathVariable Long managerId)
    {
        return taskService.createTask(request.getTitle(), request.getDescription(), request.getPriority(), managerId, request.getAssignedToUserId(), request.getDueDate());
    }

    // Employee views tasks assigned to him (Summary only)
    @GetMapping("/employee/{employeeId}")
    public List<TaskSummaryDto> getTasksForEmployee(@PathVariable Long employeeId)
    {
        return taskService.getTasksForEmployee(employeeId);
    }

    // Manager views tasks he created (Summary only)
    @GetMapping("/manager/{managerId}")
    public List<TaskSummaryDto> getTaskCreatedByManager(@PathVariable Long managerId)
    {
        return taskService.getTasksCreatedByManager(managerId);
    }

    // Employee views complete details of particular task
    @GetMapping("/employee/{employeeId}/{taskId}")
    public TaskDetailedDto getEmployeeTasksById(@PathVariable Long employeeId, @PathVariable Long taskId) {
        return taskService.getDetailedTaskByEmployeeId(employeeId, taskId);
    }

    // Manager views complete details of particular task
    @GetMapping("/manager/{managerId}/{taskId}")
    public TaskDetailedDto getManagerTasksById(@PathVariable Long managerId, @PathVariable Long taskId) {
        return taskService.getDetailedTaskByManagerId(managerId,taskId);
    }
}
