package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.UserxGroupModel;

public interface UserXGroupRepo extends JpaRepository<UserxGroupModel, Integer>{
}