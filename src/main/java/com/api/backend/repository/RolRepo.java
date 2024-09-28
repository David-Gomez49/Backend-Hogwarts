package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.backend.model.RolModel;

public interface RolRepo extends JpaRepository<RolModel, Integer>{
    
        @Query("SELECT r FROM RolModel r WHERE r.access = TRUE")
        List<RolModel> findByAccessTrue();
}