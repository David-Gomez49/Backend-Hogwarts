package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.model.CalificationModel;

public interface CalificationRepo extends JpaRepository<CalificationModel, Integer>{

    public List<CalificationModel> findByAssesment_Classes_Id(int Id);

    public List<CalificationModel> findByStudent_Email(String Email);

    public List<CalificationModel> findByStudent_IdAndAssesment_Classes_Id(int studentId, int classId);

    public List<CalificationModel> findByStudent_EmailAndAssesment_Classes_Id(String Email, int Id);

    public List<CalificationModel> findByAssesment_Classes_Teacher_Email(String Email);
    
    public CalificationModel findByStudent_IdAndAssesment_Id(int Student_Id, int Assesment_Id);

    public boolean existsById(int Id);
}