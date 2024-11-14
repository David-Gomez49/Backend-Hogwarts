package com.api.backend.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.AttendanceModel;

public interface AttendanceRepo extends JpaRepository<AttendanceModel, Integer>{

    public List<AttendanceModel> findAttendanceByclasses_Id(int Id);
    
    public List<AttendanceModel> findAttendancesByStudent_Email(String Email);

    public List<AttendanceModel> findByClasses_IdAndDate(int Id,Date Date);
}