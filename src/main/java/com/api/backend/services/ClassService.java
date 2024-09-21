package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.ClassModel;
import com.api.backend.repository.ClassRepo;

@Service
public class ClassService {

    @Autowired
    private ClassRepo classRepo;

    public List<ClassModel> obtainClassList() {
        return (List<ClassModel>) classRepo.findAll();
    }

    public ClassModel createClass(ClassModel classes) {
        return classRepo.save(classes);
    }

    public ClassModel updateClass(ClassModel classes) {
        return classRepo.save(classes);
    }

    public void deleteClass(int id) {
        classRepo.deleteById(id);
    }
}

