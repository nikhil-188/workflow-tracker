package com.workflowtracker.service;

import com.workflowtracker.dto.CommentDto;
import com.workflowtracker.dto.CreateCommentRequest;
import com.workflowtracker.entity.Comment;
import com.workflowtracker.entity.Task;
import com.workflowtracker.entity.User;
import com.workflowtracker.repository.CommentRepository;
import com.workflowtracker.repository.TaskRepository;
import com.workflowtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //this creates the constructor for DI
public class CommentService
{
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;

    //making sure only the respective employee can access the comments of the particular task
    private void validateCommentAccess(Task task, User user)
    {
        boolean isCreator = task.getCreatedBy().getId().equals(user.getId());
        boolean isAssignee = task.getAssignedTo().getId().equals(user.getId());
        if(!isCreator && !isAssignee)
        {
            throw new RuntimeException("You don't have access to this task");
        }
    }

    // Add a comment to a task
    public CommentDto addComment(Long taskId, String content )
    {
        User author = currentUserService.getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        validateCommentAccess(task,author);

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .author(author)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return commentDto(savedComment);
    }

    // Get all comments for a task
    public List<CommentDto> getCommentsForTask(Long taskId)
    {
        User currentUser = currentUserService.getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        validateCommentAccess(task,currentUser);

        List<CommentDto> listOfComments =  commentRepository.findByTask_Id(task.getId())
                .stream()
                .map(this::commentDto)
                .toList();

        return listOfComments;
    }

    //centralized mapping logic
    private CommentDto commentDto(Comment comment)
    {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName()
        );
    }
}
