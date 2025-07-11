package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.StudentXParentService;

@RestController
@RequestMapping("/studentXParent")
public class StudentXParentControl {
    @Autowired
    private StudentXParentService studentXParentService;
    @Autowired
    private JwtService jwtService;
    

    @GetMapping("/getAll")
    public List<StudentsXParentsModel> obtainStudentXParentList(@CookieValue(name = "JWT") String token) {
        if(jwtService.ValidateTokenAdmin(token)){
            return studentXParentService.obtainStudentXParentList();
        }
        return null;
    }

    @GetMapping("/getSons")
    public List<StudentsXParentsModel> obtainSonsList(@CookieValue(name = "JWT") String token) {
        
        return studentXParentService.obtainSonsList( jwtService.extractEmailFromToken(token));
        
    }

    @GetMapping("/getParents")
    public List<StudentsXParentsModel> obtainParentsList(@CookieValue(name = "JWT") String token) {
        
        return studentXParentService.obtainParentList( jwtService.extractEmailFromToken(token));
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createSubject(@CookieValue(name = "JWT") String token, @RequestBody StudentsXParentsModel studentxparent) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                studentXParentService.createStudentXParent(studentxparent);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateSubject(@CookieValue(name = "JWT") String token, @RequestBody StudentsXParentsModel studentxparent) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                studentXParentService.updateStudentXParent(studentxparent);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteSubject(@CookieValue(name = "JWT") String token, @RequestHeader("id") int id) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                studentXParentService.deleteStudentXParent(id);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }
}
