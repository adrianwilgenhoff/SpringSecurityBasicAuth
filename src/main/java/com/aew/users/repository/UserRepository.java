package com.aew.users.repository;

import com.aew.users.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

    User findByLogin(String login);
    
}