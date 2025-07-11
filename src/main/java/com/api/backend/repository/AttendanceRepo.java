package com.api.backend.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.model.AttendanceModel;

public interface AttendanceRepo extends JpaRepository<AttendanceModel, Integer>{

    public List<AttendanceModel> findAttendanceByclasses_Id(int Id);
    
    public List<AttendanceModel> findAttendancesByStudent_Email(String Email);

    public AttendanceModel findByStudent_IdAndClasses_IdAndDate(int StudentId,int ClassesId,LocalDate Date);

    public List<AttendanceModel> findByClasses_IdAndDate(int Id,Date Date);
    
    public List<AttendanceModel> getAttendancesByStudent_IdAndClasses_IdAndDate(int StudentId,int ClassesId,LocalDate Date);
}