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

import com.api.backend.model.RolModel;
import com.api.backend.services.RolService;

@RestController
@RequestMapping("/rol")
public class RolControl {
    @Autowired
    private RolService rolService;

    @GetMapping("/getAll")
    public List<RolModel> obtainRolList() {
        return rolService.obtainRolList();
    }
    
    @GetMapping("/getPublicRoles")
    public List<RolModel> obtainPublicRolList() {
        return rolService.obtainPublicRolList();
    }

    @PostMapping("/create")
    public RolModel createRol(@RequestBody RolModel rol) {
        return rolService.createRol(rol);
    }


}

