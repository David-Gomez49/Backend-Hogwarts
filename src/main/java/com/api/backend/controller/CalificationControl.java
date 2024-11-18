package com.api.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.CalificationModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.CalificationService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/calification")
public class CalificationControl {
    @Autowired
    private CalificationService calificationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentXParentService studentXParentService;

    @GetMapping("/getCalificationsListByClass")
    public List<CalificationModel> getCalificationsListByClass(@CookieValue(name = "JWT") String token, @RequestHeader("ClassId") int Id) {
        if(jwtService.ValidateTokenAdminTeacher(token)){
            return calificationService.getCalificationsListByClass(Id);
        }
        return null;
    }

    @GetMapping("/getCalificationsByEmail")
    public List<CalificationModel> getCalificationsByEmail(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();

        if ("Student".equals(rolName)) {
            return calificationService.getCalificationsByEmail(email);
        }
        if ("Parent".equals(rolName)) {
            List<CalificationModel> califications = new ArrayList<>();
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                califications.addAll(calificationService.getCalificationsByEmail(son.getStudent().getEmail()));
            }
            return califications;
        }
        return null;
    }

    @PostMapping("/CreateUpdateCalifications")
    public ResponseEntity<Boolean> CreateUpdateCalifications(@CookieValue(name = "JWT") String token, @RequestBody List<CalificationModel> califications) {
        if(jwtService.ValidateTokenAdminTeacher(token)){
            try {
                calificationService.saveOrUpdateCalifications(califications);
                return ResponseEntity.ok(true);
            } catch (Exception e) {
                return ResponseEntity.ok(false);
            }
            }
        return ResponseEntity.ok(false); 
    }
}