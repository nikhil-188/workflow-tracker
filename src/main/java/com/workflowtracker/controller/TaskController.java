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
    @PostMapping
    public TaskDetailedDto createTask(@RequestBody CreateTaskRequest request)
    {
        return taskService.createTask(request.getTitle(), request.getDescription(), request.getPriority(), request.getAssignedToUserId(), request.getDueDate());
    }

    // User views tasks assigned to him (Summary only)
    @GetMapping("/my")
    public List<TaskSummaryDto> getMyTasksSummary()
    {
        return taskService.getMyTasksSummary();
    }

    // User views complete details of particular task only assigned to him/created by him
    @GetMapping("{taskId}")
    public TaskDetailedDto getMyTasksDetailed(@PathVariable Long taskId) {
        return taskService.getMyTasksDetailed(taskId);
    }
}
