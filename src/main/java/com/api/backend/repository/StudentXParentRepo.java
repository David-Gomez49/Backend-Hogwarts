package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.StudentsXParentsModel;

public interface StudentXParentRepo extends JpaRepository<StudentsXParentsModel, Integer>{
    List<StudentsXParentsModel> findByParent_Email(String Email);

    List<StudentsXParentsModel> findByStudent_Email(String Email);
}