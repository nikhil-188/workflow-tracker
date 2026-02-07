package com.workflowtracker.service;

import com.workflowtracker.dto.CommentResponseDto;
import com.workflowtracker.entity.Comment;
import com.workflowtracker.entity.Task;
import com.workflowtracker.entity.User;
import com.workflowtracker.exception.UnauthorizedTaskAccessException;
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

    //making sure only the respective employee can access the comments of the particular task
    private void validateCommentAccess(Task task, User user) {
        boolean isCreator = task.getCreatedBy().getId().equals(user.getId());
        boolean isAssignee = task.getAssignedTo().getId().equals(user.getId());

        if (!isCreator && !isAssignee) {
            throw new UnauthorizedTaskAccessException(
                    "You are not allowed to access comments for this task"
            );

        }
    }

    // Add a comment to a task
    public CommentResponseDto addComment(Long taskId, Long authorId, String content) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        validateCommentAccess(task,author);

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .author(author)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return mapToDto(savedComment);
    }

    // Get all comments for a task
    public List<CommentResponseDto> getCommentsForTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        validateCommentAccess(task,user);

        return commentRepository.findByTask(task)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    //centralized mapping logic
    private CommentResponseDto mapToDto(Comment comment)
    {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName()
        );
    }
}
