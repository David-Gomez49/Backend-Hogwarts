package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.SubjectModel;

public interface SubjectRepo extends JpaRepository<SubjectModel, Integer>{
}