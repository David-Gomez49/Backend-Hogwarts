package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.GroupModel;

public interface GroupRepo extends JpaRepository<GroupModel, Integer>{
    
}