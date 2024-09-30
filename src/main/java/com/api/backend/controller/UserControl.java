package com.api.backend.controller;

import java.util.List;
import java.util.Map;

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

import com.api.backend.model.RolModel;
import com.api.backend.model.UserModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/getAll")
    public List<UserModel> obtainUserList() {
        return userService.obtainUserList();
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<UserModel> obtainUserByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        UserModel user = userService.obtainUserByEmail(email);
        System.out.println("--------------------");
        System.out.println(user);
        System.out.println("--------------------");

        if (user != null) {
            System.out.println("----------------(if)-----------------");
            return ResponseEntity.ok(user);

        } else {
            System.out.println("----------------(else)----------------");
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
    public ResponseEntity<Boolean> validateUser(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/updateByEmail")
    public UserModel updateUserByEmail(@RequestHeader("Authorization") String token, @RequestBody UserModel user) {
        String actualToken = token.substring(7);
        // Llamar al servicio para encontrar el usuario por email y actualizarlo
        String email = jwtService.extractEmailFromToken(actualToken);
        return userService.updateUserByEmail(email, user);
    }

    @PutMapping("/infoCompleteByEmail")
    public ResponseEntity<Boolean> infoCompleteByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return ResponseEntity.ok(userService.InfoCompleteByEmail(email));
    }

    @PutMapping("/getInfoByEmail")
    public UserModel getInfoByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return userService.getInfoByEmail(email);
    }

    @GetMapping("/listUserInfo")
    public List<UserModel> getUsersWithSpecificFields() {
        return userService.obtainUserListWithSpecificFields();
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
