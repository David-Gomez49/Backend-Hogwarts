package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.UserxGroupModel;
import com.api.backend.services.UserXGroupService;

@RestController
@RequestMapping("/userXGroup")
public class UserXGroupControl {
    @Autowired
    private UserXGroupService userXGroupService;

    @GetMapping("/getAll")
    public List<UserxGroupModel> obtainUserXGroupList() {
        return userXGroupService.obtainUserXGroupList();
    }

    @PostMapping("/create")
    public UserxGroupModel createUserXGroup(@RequestBody UserxGroupModel userXGroup) {
        return userXGroupService.createUserXGroup(userXGroup);
    }

    @PutMapping("/edit/{id}")
    public UserxGroupModel updateUserXGroup(@PathVariable int id, @RequestBody UserxGroupModel userXGroup) {
        userXGroup.setId(id);
        return userXGroupService.updateUserXGroup(userXGroup);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserXGroup(@PathVariable int id) {
        userXGroupService.deleteUserXGroup(id);
    }
}
