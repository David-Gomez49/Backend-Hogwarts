package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.model.StudentsXParentsModel;

public interface StudentXParentRepo extends JpaRepository<StudentsXParentsModel, Integer>{
    List<StudentsXParentsModel> findByParent_Email(String Email);

    List<StudentsXParentsModel> findByStudent_Email(String Email);

    void deleteByParent_Email(String Email);

    void deleteByStudent_Email(String Email);
}