package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.ClassModel;

public interface ClassRepo extends JpaRepository<ClassModel, Integer>{
}