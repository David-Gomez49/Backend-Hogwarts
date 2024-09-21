package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.StudentsXParentsModel;

public interface StudentXParentRepo extends JpaRepository<StudentsXParentsModel, Integer>{
}