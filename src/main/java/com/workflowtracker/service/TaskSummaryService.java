package com.workflowtracker.service;

import com.workflowtracker.dto.TaskSummaryDto;
import com.workflowtracker.entity.Task;
import com.workflowtracker.entity.User;
import com.workflowtracker.repository.TaskRepository;
import com.workflowtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor //this creates the constructor for DI
public class TaskSummaryService
{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Fetch tasks assigned to employee (Summary View)
    public List<TaskSummaryDto> getTasksForEmployee(Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByAssignedTo(employee)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Fetch tasks created by manager (Summary View)
    public List<TaskSummaryDto> getTasksCreatedByManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return taskRepository.findByCreatedBy(manager)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private TaskSummaryDto mapToDto(Task task) {
        return TaskSummaryDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .taskStatus(task.getStatus())
                .deadline(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .assignedTo(mapUserSummary(task.getAssignedTo()))
                .build();
    }

    private com.workflowtracker.dto.UserSummaryDto mapUserSummary(User user) {
        if (user == null)
            return null;
        return com.workflowtracker.dto.UserSummaryDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
