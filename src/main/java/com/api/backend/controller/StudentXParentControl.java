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

import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.services.StudentXParentService;

@RestController
@RequestMapping("/studentXParent")
public class StudentXParentControl {
    @Autowired
    private StudentXParentService studentXParentService;

    @GetMapping("/getAll")
    public List<StudentsXParentsModel> obtainStudentXParentList() {
        return studentXParentService.obtainStudentXParentList();
    }

    @PostMapping("/create")
    public StudentsXParentsModel createStudentXParent(@RequestBody StudentsXParentsModel studentXParent) {
        return studentXParentService.createStudentXParent(studentXParent);
    }

    @PutMapping("/edit/{id}")
    public StudentsXParentsModel updateStudentXParent(@PathVariable int id, @RequestBody StudentsXParentsModel studentXParent) {
        studentXParent.setId(id);
        return studentXParentService.updateStudentXParent(studentXParent);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudentXParent(@PathVariable int id) {
        studentXParentService.deleteStudentXParent(id);
    }
}
