package com.api.backend.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.GroupModel;
import com.api.backend.repository.AssesmentRepo;

@Service
public class AssesmentService  {

    @Autowired
    private AssesmentRepo assesmentRepo;

    public List<AssesmentModel> obtainAssesmentList() {
        return (List<AssesmentModel>) assesmentRepo.findAll();
    }

    public AssesmentModel createAssesment(AssesmentModel assesment) {
        return assesmentRepo.save(assesment);
    }

    public AssesmentModel updateAssesment(AssesmentModel assesment) {
        return assesmentRepo.save(assesment);
    }

    public void deleteAssesment(int id) {
        assesmentRepo.deleteById(id);
    }

    public List<AssesmentModel> obtainAssestmentsByTeacher(String email) {
        return assesmentRepo.findByClasses_Teacher_Email(email);
    }

    public List<AssesmentModel> obtainAssestmentsByGroup(GroupModel group) {
        return assesmentRepo.findAssessmentsByGroup(group);
}
    public List<AssesmentModel> obtainAssestmentsByClass(int classId) {
        return assesmentRepo.findByClasses_Id(classId);
    }
}


