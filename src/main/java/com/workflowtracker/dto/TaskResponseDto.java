package com.workflowtracker.dto;

import com.workflowtracker.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskResponseDto
{
    private Long id;
    private String title;
    private String description;

    private TaskStatus status;
    private Priority priority;

    private LocalDate dueDate;
    private LocalDateTime createdAt;

    private UserResponseDto createdBy;
    private UserResponseDto assignedTo;
}
