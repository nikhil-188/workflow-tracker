package com.workflowtracker.dto;

import com.workflowtracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSummaryDto {
    private Long id;
    private String title;
    private TaskStatus taskStatus;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private UserInTaskViewDto assignedTo;
}
