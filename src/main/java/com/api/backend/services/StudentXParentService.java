package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.repository.StudentXParentRepo;

@Service
public class StudentXParentService {

    @Autowired
    private StudentXParentRepo studentxparentRepo;

    public List<StudentsXParentsModel> obtainStudentXParentList() {
        return (List<StudentsXParentsModel>) studentxparentRepo.findAll();
    }

    public StudentsXParentsModel createStudentXParent(StudentsXParentsModel studentxparent) {
        return studentxparentRepo.save(studentxparent);
    }

    public StudentsXParentsModel updateStudentXParent(StudentsXParentsModel studentxparent) {
        return studentxparentRepo.save(studentxparent);
    }

    public void deleteStudentXParent(int id) {
        studentxparentRepo.deleteById(id);
    }
}
