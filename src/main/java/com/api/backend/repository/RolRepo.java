package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.backend.model.RolModel;

public interface RolRepo extends JpaRepository<RolModel, Integer>{
    
        RolModel findByName(String name);
        
        @Query("SELECT r FROM RolModel r WHERE r.access = TRUE")
        List<RolModel> findByAccessTrue();
}