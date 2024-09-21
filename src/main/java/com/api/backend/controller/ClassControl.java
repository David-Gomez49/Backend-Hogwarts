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

import com.api.backend.model.ClassModel;
import com.api.backend.services.ClassService;

@RestController
@RequestMapping("/class")
public class ClassControl {
    @Autowired
    private ClassService classService;

    @GetMapping("/getAll")
    public List<ClassModel> obtainClassList() {
        return classService.obtainClassList();
    }

    @PostMapping("/create")
    public ClassModel createClass(@RequestBody ClassModel classes) {
        return classService.createClass(classes);
    }

    @PutMapping("/edit/{id}")
    public ClassModel updateClass(@PathVariable int id, @RequestBody ClassModel classes) {
        classes.setId(id);
        return classService.updateClass(classes);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClass(@PathVariable int id) {
        classService.deleteClass(id);
    }
}
