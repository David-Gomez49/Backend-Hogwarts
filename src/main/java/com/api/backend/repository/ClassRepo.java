package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.ClassModel;
import com.api.backend.model.GroupModel;

public interface ClassRepo extends JpaRepository<ClassModel, Integer>{
    List<ClassModel> findByTeacherEmail(String email);
    
    List<ClassModel> findByGroup(GroupModel group);

}