package com.workflowtracker.controller;

import com.workflowtracker.dto.CommentDto;
import com.workflowtracker.dto.CreateCommentRequest;
import com.workflowtracker.entity.User;
import com.workflowtracker.service.CommentService;
import com.workflowtracker.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
@RequiredArgsConstructor //this creates the constructor for DI
public class CommentController
{
    private final CommentService commentService;
    private final CurrentUserService currentUserService;

    // Add comment to a task
    @PostMapping
    public CommentDto addComment(@PathVariable Long taskId, @RequestBody CreateCommentRequest request)
    {
        return commentService.addComment(taskId, request.getContent());
    }

    // Get all comments for a task
    @GetMapping
    public List<CommentDto> getCommentsForTask(@PathVariable Long taskId)
    {
        return commentService.getCommentsForTask(taskId);
    }
}
