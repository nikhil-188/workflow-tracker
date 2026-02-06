package com.workflowtracker.controller;

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
    public Comment addComment(
            @PathVariable Long taskId,
            @RequestParam Long authorId,
            @RequestParam String content
    ) {
        return commentService.addComment(taskId, authorId, content);
    }

    // Get all comments for a task
    @GetMapping
    public List<Comment> getCommentsForTask(@PathVariable Long taskId) {
        return commentService.getCommentsForTask(taskId);
    }
}
