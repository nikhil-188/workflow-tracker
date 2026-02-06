package com.workflowtracker.dto;

import com.workflowtracker.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto
{
    private Long id;
    private String name;
    private String email;
    private Role role;
}
