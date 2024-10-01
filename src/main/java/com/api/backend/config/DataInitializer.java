package com.api.backend.config;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.backend.model.RolModel;
import com.api.backend.model.UserModel;
import com.api.backend.services.RolService;
import com.api.backend.services.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private RolService rolService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        // Verificar y agregar roles si no existen
        if (rolService.obtainRolList().isEmpty()) {
            rolService.createRol(new RolModel(0, true,"Student"));
            rolService.createRol(new RolModel(0, true,"Parent"));
            rolService.createRol(new RolModel(0, false,"Teacher"));
            rolService.createRol(new RolModel(0, false,"Admin"));
        }

        // Crear el usuario solo si no existen usuarios
        if (userService.obtainUserList().isEmpty()) {
            userService.createUser(new UserModel(
                0, // ID se genera automáticamente
                "Santiago", 
                "Trespalacios", 
                Date.valueOf("2003-02-09"), 
                "Masculino", 
                "Calle 123", 
                "123456789", 
                "santiagot3p@gmail.com", 
                "CC", 
                "1234567890",
                rolService.obtainRolList().stream().filter(rol -> rol.getName().equals("Admin")).findFirst().orElse(null) // Asignar rol Admin
                ,"noen"
            ));
            for (int i = 1; i <= 10; i++) {
                userService.createUser(new UserModel(
                    0, // ID se genera automáticamente
                    "Usuario" + i, 
                    "Apellido" + i, 
                    Date.valueOf("1990-01-0" + i), // Fechas de ejemplo
                    "Masculino", 
                    "Calle " + (100 + i), 
                    "123456789" + i, 
                    "usuario", 
                    "CC", 
                    "1234567890" + i, 
                    rolService.obtainRolList().stream().filter(rol -> rol.getName().equals("Student")).findFirst().orElse(null), // Asignar rol Admin
                    "picture"
                ));
            }
            
        }
    }
}
