package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.AttendanceModel;

public interface AttendanceRepo extends JpaRepository<AttendanceModel, Integer>{
}