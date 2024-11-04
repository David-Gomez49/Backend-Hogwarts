package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.GroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.GroupService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/group")
public class GroupControl {
    @Autowired
    private GroupService groupService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<GroupModel> obtainClasstList(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        boolean valid = userService.validateAdmin(email);
        if (valid) {
            return groupService.obtainGroupList();
        }
        return null;
    }
}
