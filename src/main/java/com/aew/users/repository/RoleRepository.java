package com.aew.users.repository;

import com.aew.users.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Authority,String>{}