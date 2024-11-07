package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.RolModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.AssesmentService;
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

    @GetMapping("/getMyAssesments")
    public List<AssesmentModel> obtainMyAssesmentsList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        RolModel rol = userService.GetRolByEmail(email);
        if ((rol.getName().equals("Admin"))) {
            return assesmentService.obtainAssesmentList();
        }
        if (( (rol.getName().equals("Teacher")))){
            return assesmentService.obtainAssestmentsByTeacher(email);  // 1
        }
        if (rol.getName().equals("Student")){
            UserxGroupModel userGroup = userGroupService.findByStudent(userService.obtainUserByEmail(email));
             if (userGroup != null) {
                GroupModel group = userGroup.getGroup();
                return assesmentService.obtainAssestmentsByGroup(group); // 2
            }

        }
            return null;
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
}
