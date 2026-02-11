package com.workflowtracker.controller;

import com.workflowtracker.dto.CommentResponseDto;
import com.workflowtracker.entity.*;
import com.workflowtracker.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Add comment to a task
    @PostMapping
    public CommentResponseDto addComment(
            @PathVariable Long taskId,
            @RequestParam Long authorId,
            @RequestParam String content) {
        System.out.println(
                "DEBUG: addComment called. TaskID: " + taskId + ", AuthorID: " + authorId + ", Content: " + content);
        return commentService.addComment(taskId, authorId, content);
    }

    // Get all comments for a task
    @GetMapping
    public List<CommentResponseDto> getCommentsForTask(@PathVariable Long taskId, @RequestParam Long userId) {
        return commentService.getCommentsForTask(taskId, userId);
    }
}
