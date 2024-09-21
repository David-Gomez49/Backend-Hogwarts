package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.UserModel;

public interface UserRepo extends JpaRepository<UserModel, Integer>{
}
