package com.api.backend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.RolModel;
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
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserXGroupService userGroupService;
    @Autowired
    private StudentXParentService studentXParentService;


    @GetMapping("/getMyAssesments")
public List<AssesmentModel> obtainMyAssesmentsList(@RequestHeader("Authorization") String token) {
    String actualToken = token.substring(7);
    String email = jwtService.extractEmailFromToken(actualToken);
    RolModel rol = userService.GetRolByEmail(email);
    
    if ("Admin".equals(rol.getName())) {
        return assesmentService.obtainAssesmentList();
    }
    
    if ("Teacher".equals(rol.getName())) {
        return assesmentService.obtainAssestmentsByTeacher(email);
    }
    
    if ("Student".equals(rol.getName())) {
        UserxGroupModel userGroup = userGroupService.findByStudent(userService.obtainUserByEmail(email));
        if (userGroup != null) {
            GroupModel group = userGroup.getGroup();
            return assesmentService.obtainAssestmentsByGroup(group);
        }
    }
    
    if ("Parent".equals(rol.getName())) {
        Set<AssesmentModel> assesments = new HashSet<>(); 
        List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
        for (StudentsXParentsModel son : sons) {
            UserxGroupModel userGroup = userGroupService.findByStudent(son.getStudent());
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
    public List<AssesmentModel> obtainAssesmentsListByClass(@RequestHeader("Authorization") String token,@RequestHeader("ClassId") int id) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        if(userService.existsByEmail(email)){
            return assesmentService.obtainAssestmentsByClass(id); // 3
        }
            return null;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createClass(@RequestHeader("Authorization") String token, @RequestBody AssesmentModel assesment) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid_teacher = userService.validateTeacher(email);
        boolean valid_admin = userService.validateAdmin(email);
        if (valid_teacher||valid_admin) {
            assesmentService.createAssesment(assesment);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
