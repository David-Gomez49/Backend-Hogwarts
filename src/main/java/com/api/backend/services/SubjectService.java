package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.SubjectModel;
import com.api.backend.repository.SubjectRepo;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    public List<SubjectModel> obtainSubjectList() {
        return (List<SubjectModel>) subjectRepo.findAll();
    }

    public SubjectModel createSubject(SubjectModel subject) {
        return subjectRepo.save(subject);
    }

    public SubjectModel updateSubject(SubjectModel subject) {
        return subjectRepo.save(subject);
    }

    public void deleteSubject(int id) {
        subjectRepo.deleteById(id);
    }
}
