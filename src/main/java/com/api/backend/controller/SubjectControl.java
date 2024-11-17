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

import com.api.backend.model.SubjectModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.SubjectService;

@RestController
@RequestMapping("/subject")
public class SubjectControl {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/getAll")
    public List<SubjectModel> obtainSubjectList() {
        return subjectService.obtainSubjectList();
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createSubject(@CookieValue(name = "JSESSIONID") String token, @RequestBody SubjectModel subject) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                subjectService.createSubject(subject);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateSubject(@CookieValue(name = "JSESSIONID") String token, @RequestBody SubjectModel subject) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                subjectService.updateSubject(subject);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteSubject(@CookieValue(name = "JSESSIONID") String token, @RequestHeader("id") int id) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                subjectService.deleteSubject(id);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }
}