package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.SubjectModel;
import com.api.backend.model.UserModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.SubjectService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/subject")
public class SubjectControl {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<SubjectModel> obtainSubjectList() {
        return subjectService.obtainSubjectList();
    }

    @PostMapping("/create")
    public List<UserModel> createSubject(@RequestHeader("Authorization") String token, @RequestBody SubjectModel subject) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            subjectService.createSubject(subject);
        }
        return null;
    }

    @PutMapping("/update")
    public List<UserModel> updateSubject(@RequestHeader("Authorization") String token, @RequestBody SubjectModel subject) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            subjectService.updateSubject(subject);
        }
        return null;
    }

    @DeleteMapping("/delete")
    public List<UserModel> deleSubject(@RequestHeader("Authorization") String token,  @RequestHeader("id") int id) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            subjectService.deleteSubject(id);
        }
        return null;
    }

}
