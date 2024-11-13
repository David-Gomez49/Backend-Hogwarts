package com.api.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.backend.model.GroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.GroupService;

@RestController
@RequestMapping("/group")
public class GroupControl {
    @Autowired
    private GroupService groupService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/getAll")
    public List<GroupModel> obtainClasstList(@CookieValue("token") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return groupService.obtainGroupList();
        }
        return null;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Boolean> obtainClasstList(@CookieValue("token") String token, @RequestBody GroupModel group) {
        if (jwtService.ValidateTokenAdmin(token)) {
            groupService.createGroup(group);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
