package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AuxiliarUserModel;
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
    public List<UserModel> obtainUserList(@RequestHeader("Authorization") String token) {
      
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainUserList();
        }
        return null;
    }

    @GetMapping("/getTeachers")
    public List<UserModel> obtainTeacherList(@RequestHeader("Authorization") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainTeacherList();
        }
        return null;
    }

    @GetMapping("/getParents")
    public List<UserModel> obtainParentsList(@RequestHeader("Authorization") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainParentsList();
        }
        return null;
    }

    @GetMapping("/getStudents")
    public List<UserModel> obtainStudentsList(@RequestHeader("Authorization") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainStudentsList();
        }
        return null;
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<UserModel> obtainUserByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
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
    public ResponseEntity<Boolean> validateUser(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validateAdmin")
    public ResponseEntity<Boolean> validateAdmin(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean valid = userService.validateAdmin(email);
        return ResponseEntity.ok(valid);
    }

    @GetMapping("/validateTeachersAdmins")
    public ResponseEntity<Boolean> validateTeachersAdmins(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean valid = userService.validateAdmin(email);
        boolean validteacher=userService.validateTeacher(email);
        return ResponseEntity.ok(valid||validteacher);
    }
    


    @PutMapping("/updateByEmail")
    public UserModel updateUserByEmail(@RequestHeader("Authorization") String token, @RequestBody UserModel user) {
        String actualToken = token.substring(7);
        // Llamar al servicio para encontrar el usuario por email y actualizarlo
        String email = jwtService.extractEmailFromToken(actualToken);
        return userService.updateUserByEmail(email, user);
    }

    @GetMapping("/infoCompleteByEmail")
    public ResponseEntity<Boolean> infoCompleteByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return ResponseEntity.ok(userService.InfoCompleteByEmail(email));
    }

    @GetMapping("/getInfoByEmail")
    public AuxiliarUserModel getInfoByEmail(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        String email = jwtService.extractEmailFromToken(actualToken);
        return userService.getInfoByEmail(email);
    }

    @GetMapping("/listUserInfo")
    public List<AuxiliarUserModel> getUsersWithSpecificFields(@RequestHeader("Authorization") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainUserListWithSpecificFields();
        }
        return null;
    }

    @DeleteMapping("/deleteUserByEmail")
    public ResponseEntity<Boolean> deleteUserByEmail(
        @RequestHeader("Authorization") String token, 
        @RequestHeader("user") String email) {
 
        try {
            String actualToken = token.substring(7); // Elimina el "Bearer " del token
            String emailAdmin = jwtService.extractEmailFromToken(actualToken);
            
            boolean valid = userService.validateAdmin(emailAdmin);
            boolean userValid = userService.existsByEmail(email);
            if ((!valid || !userValid )|| emailAdmin.equals(email)) {
                return ResponseEntity.ok(false); // No tienes permisos
            }
          
            userService.deleteByEmail(email);
            return ResponseEntity.ok(true); // Usuario eliminado exitosamente
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Error del servidor
        }
    }

    @PutMapping("/editRolByEmail")
    public ResponseEntity<Boolean> EditRolByEmail(
        @RequestHeader("Authorization") String token, 
        @RequestHeader("user") String email,
        @RequestHeader("role") String newrole) {

        try {
            String actualToken = token.substring(7); // Elimina el "Bearer " del token
            String emailAdmin = jwtService.extractEmailFromToken(actualToken);
            
            boolean valid = userService.validateAdmin(emailAdmin);
            boolean userValid = userService.existsByEmail(email);
            if ((!valid || !userValid )|| emailAdmin.equals(email)) {
                return ResponseEntity.ok(false);
            }
            userService.editRolByEmail(email, newrole);
            return ResponseEntity.ok(true); // Usuario eliminado exitosamente
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Error del servidor
        }
    }
    


}
