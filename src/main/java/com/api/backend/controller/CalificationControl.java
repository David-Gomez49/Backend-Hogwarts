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

import com.api.backend.model.CalificationModel;
import com.api.backend.services.CalificationService;

@RestController
@RequestMapping("/calification")
public class CalificationControl {
    @Autowired
    private CalificationService calificationService;

    @GetMapping("/getAll")
    public List<CalificationModel> obtainCalificationList() {
        return calificationService.obtainCalificationList();
    }

    @PostMapping("/create")
    public CalificationModel createCalification(@RequestBody CalificationModel calification) {
        return calificationService.createCalification(calification);
    }

    @PutMapping("/edit/{id}")
    public CalificationModel updateCalification(@PathVariable int id, @RequestBody CalificationModel calification) {
        calification.setId(id);
        return calificationService.updateCalification(calification);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCalification(@PathVariable int id) {
        calificationService.deleteCalification(id);
    }
}
