package com.workflowtracker.service;

import com.workflowtracker.entity.*;
import com.workflowtracker.repository.CommentRepository;
import com.workflowtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService
{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    //Add a comment to task
    public Comment addComment(Long taskId, String content, User author)
    {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .author(author)
                .build();

        //createdAt handled by @PrePersist
        return commentRepository.save(comment);
    }
    //Get all comments for a task
    public List<Comment> getCommentsForTask(Task task)
    {
        return commentRepository.findByTask(task);
    }
}
