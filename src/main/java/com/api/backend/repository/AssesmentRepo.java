package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.AssesmentModel;

public interface AssesmentRepo extends JpaRepository<AssesmentModel, Integer>{
}
