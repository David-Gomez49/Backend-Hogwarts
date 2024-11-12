package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.StudentWithGroupModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.model.SubjectModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/studentXParent")
public class StudentXParentControl {
    @Autowired
    private StudentXParentService studentXParentService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<StudentsXParentsModel> obtainStudentXParentList(@RequestHeader("Authorization") String token) {
        
        if(jwtService.ValidateTokenAdmin(token)){
            return studentXParentService.obtainStudentXParentList();
        }
        return null;
    }

    @GetMapping("/getSons")
    public List<StudentsXParentsModel> obtainSonsList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return studentXParentService.obtainSonsList(email);
        
    }

    @GetMapping("/getParents")
    public List<StudentsXParentsModel> obtainParentsList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return studentXParentService.obtainParentList(email);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createSubject(@RequestHeader("Authorization") String token, @RequestBody StudentsXParentsModel studentxparent) {

        if (jwtService.ValidateTokenAdmin(token)) {
            studentXParentService.createStudentXParent(studentxparent);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateSubject(@RequestHeader("Authorization") String token, @RequestBody StudentsXParentsModel studentxparent) {
        if (jwtService.ValidateTokenAdmin(token)) {
            studentXParentService.updateStudentXParent(studentxparent);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false); 
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleSubject(@RequestHeader("Authorization") String token,  @RequestHeader("id") int id) {

        if (jwtService.ValidateTokenAdmin(token)) {
            studentXParentService.deleteStudentXParent(id);
            return ResponseEntity.ok(true); 
        }
        return ResponseEntity.ok(false); 
    }
}
