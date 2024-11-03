package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.ClassModel;
import com.api.backend.model.UserModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.ClassService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/class")
public class ClassControl {
    @Autowired
    private ClassService classService;
    
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<ClassModel> obtainClasstList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            return classService.obtainClassList();
        }
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createSubject(@RequestHeader("Authorization") String token, @RequestBody ClassModel classes) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            classService.createClass(classes);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public List<UserModel> updateSubject(@RequestHeader("Authorization") String token, @RequestBody ClassModel classes) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            classService.updateClass(classes);
        }
        return null;
    }

    @DeleteMapping("/delete")
    public List<UserModel> deleSubject(@RequestHeader("Authorization") String token,  @RequestHeader("id") int id) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            classService.deleteClass(id);
        }
        return null;
    }
}
