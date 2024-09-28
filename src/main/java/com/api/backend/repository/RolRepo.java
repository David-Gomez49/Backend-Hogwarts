package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.RolModel;

public interface RolRepo extends JpaRepository<RolModel, Integer>{
    List<RolModel> findByAccessTrue();
}