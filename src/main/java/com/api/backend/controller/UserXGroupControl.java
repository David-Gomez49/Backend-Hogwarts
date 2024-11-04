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
import com.api.backend.model.UserxGroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.UserService;
import com.api.backend.services.UserXGroupService;

@RestController
@RequestMapping("/userXGroup")
public class UserXGroupControl {
    @Autowired
    private UserXGroupService userXGroupService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/student-groups")
    public List<StudentWithGroupModel> getStudentsWithGroups(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
        return userXGroupService.getAllStudentsWithGroup();
        }
        return null;
    }

    @GetMapping("/updateGroupById")
    public ResponseEntity<Boolean> getStudentsWithGroups(@RequestHeader("Authorization") String token,@RequestHeader("StudentId")int studentId,@RequestHeader("GroupId")int groupId) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            userXGroupService.assignOrUpdateGroup(studentId, groupId);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }


}
