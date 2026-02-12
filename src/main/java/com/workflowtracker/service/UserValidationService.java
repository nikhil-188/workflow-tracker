package com.workflowtracker.service;

import com.workflowtracker.entity.Role;
import com.workflowtracker.entity.User;
import com.workflowtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserValidationService
{
    private final UserRepository userRepository;

    public User validateManager(Long managerId)
    {
        User manager = userRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));
        if(manager.getRole()!= Role.MANAGER)
        {
            throw new RuntimeException("User is not manager");
        }
        return manager;
    }
    public User validateEmployee(Long employeeId)
    {
        User employee = userRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("User not found"));
        if(employee.getRole()!=Role.EMPLOYEE)
        {
            throw new RuntimeException("User is not employee");
        }
        return employee;
    }

}
