package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.GroupModel;

public interface AssesmentRepo extends JpaRepository<AssesmentModel, Integer>{

    
    List<AssesmentModel> findByClasses_Teacher_Email(String email);

    @Query("SELECT a FROM AssesmentModel a JOIN a.classes c WHERE c.group = :group")
    List<AssesmentModel> findAssessmentsByGroup(@Param("group") GroupModel group);


    public List<AssesmentModel> findByClasses_Id(int classId);

    public AssesmentModel findById(int Id);
}
