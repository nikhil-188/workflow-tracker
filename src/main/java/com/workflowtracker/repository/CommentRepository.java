package com.workflowtracker.repository;

import com.workflowtracker.entity.Comment;
import com.workflowtracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    // All the comments for a taskId
    List<Comment> findByTask_Id(Long id);
}
