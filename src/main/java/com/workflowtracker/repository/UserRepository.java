package com.workflowtracker.repository;

import com.workflowtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    //find user by email, useful for login later
    User findByEmail(String email);
}