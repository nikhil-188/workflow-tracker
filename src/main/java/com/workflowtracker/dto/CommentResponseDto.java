package com.workflowtracker.dto;

import com.workflowtracker.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto
{
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    private Long taskId;

    private Long authorId;
    private String authorName;
}
