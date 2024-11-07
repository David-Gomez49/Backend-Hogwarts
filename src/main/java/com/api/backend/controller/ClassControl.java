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
import com.api.backend.model.GroupModel;
import com.api.backend.model.RolModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.ClassService;
import com.api.backend.services.UserService;
import com.api.backend.services.UserXGroupService;

@RestController
@RequestMapping("/class")
public class ClassControl {
    @Autowired
    private ClassService classService;
    
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserXGroupService userGroupService;

    @GetMapping("/getAll")
    public List<ClassModel> obtainClassList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            return classService.obtainClassList();
        }
        return null;
    }

    @GetMapping("/getMyClasses")
    public List<ClassModel> obtainMyClassList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        RolModel rol = userService.GetRolByEmail(email);
        if ((rol.getName().equals("Admin"))) {
            return classService.obtainClassList();
        }
        if (( (rol.getName().equals("Teacher")))){
            return classService.obtainClassByTeacher(email);
        }
        if (rol.getName().equals("Student")){
            UserxGroupModel userGroup = userGroupService.findByStudent(userService.obtainUserByEmail(email));
             if (userGroup != null) {
                GroupModel group = userGroup.getGroup();
                return classService.obtainClassByGroup(group);
            }

        }
            return null;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createClass(@RequestHeader("Authorization") String token, @RequestBody ClassModel classes) {
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
    public ResponseEntity<Boolean> updateClass(@RequestHeader("Authorization") String token, @RequestBody ClassModel classes) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            classService.updateClass(classes);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteClass(@RequestHeader("Authorization") String token,  @RequestHeader("id") int id) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            classService.deleteClass(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
