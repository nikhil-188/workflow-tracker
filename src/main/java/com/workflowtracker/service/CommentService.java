package com.workflowtracker.service;

import com.workflowtracker.entity.Comment;
import com.workflowtracker.entity.Task;
import com.workflowtracker.entity.User;
import com.workflowtracker.repository.CommentRepository;
import com.workflowtracker.repository.TaskRepository;
import com.workflowtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a comment to a task
    public Comment addComment(Long taskId, Long authorId, String content) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .author(author)
                .build();

        return commentRepository.save(comment);
    }

    // Get all comments for a task
    public List<Comment> getCommentsForTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return commentRepository.findByTask(task);
    }
}
