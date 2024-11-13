package com.api.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.backend.model.RolModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.RolService;

@RestController
@RequestMapping("/rol")
public class RolControl {
    @Autowired
    private RolService rolService;
    @Autowired
    private JwtService jwtService;
    
    @GetMapping("/getAll")
    public List<RolModel> obtainRolList() {
        return rolService.obtainRolList();
    }
    
    @GetMapping("/getPublicRoles")
    public List<RolModel> obtainPublicRolList() {
        return rolService.obtainPublicRolList();
    }

    @PostMapping("/create")
    public RolModel createRol(@CookieValue(name = "token") String token, @RequestBody RolModel rol) {
        if(jwtService.ValidateTokenAdmin(token)){
            return rolService.createRol(rol);
        }
        return null;
    }
}
