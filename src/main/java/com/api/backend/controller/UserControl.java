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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.UserModel;
import com.api.backend.services.UserService;


@RestController
@RequestMapping("/user")
public class UserControl {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<UserModel> obtainUserList() {
        return userService.obtainUserList();
    }

    
    @GetMapping("/getByEmail")
    public ResponseEntity<UserModel> obtainUserByEmail(@RequestParam String email) {
    UserModel user = userService.obtainUserByEmail(email);
    if (user != null) {
        return ResponseEntity.ok(user);
    } else {
        return ResponseEntity.notFound().build();
    }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserModel user) {
    String email = user.getEmail();
    if (userService.existsByEmail(email)) {
        return ResponseEntity.badRequest().body("El correo ya está en uso.");
    }
    // Crear el usuario si el correo no existe
    UserModel newUser = userService.createUser(user);
    return ResponseEntity.ok(newUser);
    }

    @GetMapping("/validateUser")
    public ResponseEntity<Boolean> validateUser(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }


    @PutMapping("/edit/{id}")
    public UserModel updateUser(@PathVariable int id, @RequestBody UserModel user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

}
