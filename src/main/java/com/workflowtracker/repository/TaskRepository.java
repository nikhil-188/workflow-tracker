package com.workflowtracker.repository;

import com.workflowtracker.entity.Task;
import com.workflowtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
    //Tasks assigned to a specific employee
    List<Task> findByAssignedTo(User user);

    //Tasks created by manager
    List<Task> findByCreatedBy(User user);
}
