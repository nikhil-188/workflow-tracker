package com.workflowtracker.dto;

import com.workflowtracker.entity.Priority;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest
{
    private String title;
    private String description;
    private Priority priority;
    private Long assignedToUserId;
    private LocalDate dueDate;
}
