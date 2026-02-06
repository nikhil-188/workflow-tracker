package com.workflowtracker.controller;

import com.workflowtracker.dto.TaskResponseDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{
    @Autowired
    private TaskService taskService;

    //Manager creates tasks
    @PostMapping
    public TaskResponseDto createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Priority priority,
            @RequestParam Long assignedToUserId,
            @RequestParam String dueDate
            )
    {
        //Temporary hardcoded manager (later replaced by spring security)
        Long managerId = 1L; //assuming manager id = 1

        LocalDate parsedDueDate = LocalDate.parse(dueDate);

        return taskService.createTask(title,description,priority,1L,assignedToUserId,parsedDueDate);
    }

    //Employee views tasks assigned to him
    @GetMapping("/employee/{employeeId}")
    public List<TaskResponseDto> getTasksForEmployee(@PathVariable Long employeeId)
    {
        return taskService.getTasksForEmployee(employeeId);
    }

    //Manager views tasks he created
    @GetMapping("/manager/{managerId}")
    public List<TaskResponseDto> getTaskCreatedByManager(@PathVariable Long managerId)
    {
        return taskService.getTasksCreatedByManager(managerId);
    }
}
