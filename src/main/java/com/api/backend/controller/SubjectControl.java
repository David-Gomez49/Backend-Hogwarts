package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.SubjectModel;
import com.api.backend.services.SubjectService;

@RestController
@RequestMapping("/subject")
public class SubjectControl {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/getAll")
    public List<SubjectModel> obtainSubjectList() {
        return subjectService.obtainSubjectList();
    }

    @PostMapping("/create")
    public SubjectModel createSubject(@RequestBody SubjectModel subject) {
        return subjectService.createSubject(subject);
    }

    @PutMapping("/edit/{id}")
    public SubjectModel updateSubject(@PathVariable int id, @RequestBody SubjectModel subject) {
        subject.setId(id);
        return subjectService.updateSubject(subject);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSubject(@PathVariable int id) {
        subjectService.deleteSubject(id);
    }
}
