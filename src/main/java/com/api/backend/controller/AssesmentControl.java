package com.api.backend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.AssesmentService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;
import com.api.backend.services.UserXGroupService;

@RestController
@RequestMapping("/assesment")
public class AssesmentControl {
    @Autowired
    private AssesmentService assesmentService;

    @Autowired
    private StudentXParentService studentXParentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserXGroupService userXGroupService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/getMyAssesments")
    public List<AssesmentModel> obtainMyAssesmentsList(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String nameRol = userService.GetRolByEmail(email).getName();
        if ("Admin".equals(nameRol)) {
            return assesmentService.obtainAssesmentList();
        }

        if ("Teacher".equals(nameRol)) {
            return assesmentService.obtainAssestmentsByTeacher(email);
        }

        if ("Student".equals(nameRol)) {
            UserxGroupModel userGroup = userXGroupService.findByStudent(userService.obtainUserByEmail(email));
            if (userGroup != null) {
                GroupModel group = userGroup.getGroup();
                return assesmentService.obtainAssestmentsByGroup(group);
            }
        }

        if ("Parent".equals(nameRol)) {
            Set<AssesmentModel> assesments = new HashSet<>();
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                UserxGroupModel userGroup = userXGroupService.findByStudent(son.getStudent());
                if (userGroup != null) {
                    GroupModel group = userGroup.getGroup();
                    assesments.addAll(assesmentService.obtainAssestmentsByGroup(group));
                }
            }
            return new ArrayList<>(assesments);
        }

        return new ArrayList<>();
    }

    @GetMapping("/getAssesmentsByClass")
    public List<AssesmentModel> obtainAssesmentsListByClass(@CookieValue(name = "JWT") String token, @RequestHeader("ClassId") int id) {
        String email = jwtService.extractEmailFromToken(token);
        if (userService.existsByEmail(email)) {
            return assesmentService.obtainAssestmentsByClass(id);
        }
        return null;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Boolean> createAssesment(@CookieValue(name = "JWT") String token, @RequestBody AssesmentModel assesment) {
        try {
            if (jwtService.ValidateTokenAdminTeacher(token)) {
                assesmentService.createAssesment(assesment);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateAssesment(@CookieValue(name = "JWT") String token, @RequestBody AssesmentModel assesment) {
        try {
            if (jwtService.ValidateTokenAdminTeacher(token)) {
                assesmentService.updateAssesment(assesment);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAssesment(@CookieValue(name = "JWT") String token, @RequestHeader("id") int id) {
        try {
            if (jwtService.ValidateTokenAdminTeacher(token)) {
                assesmentService.deleteAssesment(id);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }
}
