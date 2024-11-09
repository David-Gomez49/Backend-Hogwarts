package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.CalificationModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.CalificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/calification")
public class CalificationControl {
    @Autowired
    private CalificationService calificationService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/getCalificationsListByClass")
    public List<CalificationModel> getCalificationsListByClass(@RequestHeader("Authorization") String token, @RequestHeader("ClassId") int Id) {
        if(jwtService.ValidateTokenAdminTeacher(token)){
        return calificationService.getCalificationsListByClass(Id);
        }
        return null;
    }

    @GetMapping("/getCalificationsListByClass")
    public List<CalificationModel> getCalificationsByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return calificationService.getCalificationsByEmail(email);
        
    }

    @PostMapping("/CreateUpdateCalifications")
    public ResponseEntity<Boolean> CreateUpdateCalifications(@RequestHeader("Authorization") String token,@RequestBody List<CalificationModel> califications) {
        try {
            calificationService.saveOrUpdateCalifications(califications);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

        
}
