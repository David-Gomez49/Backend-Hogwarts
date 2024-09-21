package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.CalificationModel;

public interface CalificationRepo extends JpaRepository<CalificationModel, Integer>{
}