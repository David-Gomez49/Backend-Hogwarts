package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AuxiliarUserModel;
import com.api.backend.model.MessageModel;
import com.api.backend.model.NotificationModel;
import com.api.backend.model.UserModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.ActiveUserService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ActiveUserService activeUserService;

    @GetMapping("/getAll")
    public List<UserModel> obtainUserList(@CookieValue(name = "JWT") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainUserList();
        }
        return null;
    }

    @GetMapping("/getTeachers")
    public List<UserModel> obtainTeacherList(@CookieValue(name = "JWT") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainTeacherList();
        }
        return null;
    }

    @GetMapping("/getParents")
    public List<UserModel> obtainParentsList(@CookieValue(name = "JWT") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainParentsList();
        }
        return null;
    }

    @GetMapping("/getStudents")
    public List<UserModel> obtainStudentsList(@CookieValue(name = "JWT") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainStudentsList();
        }
        return null;
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<UserModel> obtainUserByEmail(@CookieValue(name = "JWT") String token) {
        UserModel user = userService.obtainUserByEmail(jwtService.extractEmailFromToken(token));
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
        UserModel newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/validateUser")
    public ResponseEntity<Boolean> validateUser(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validateAdmin")
    public ResponseEntity<Boolean> validateAdmin(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean valid = userService.validateAdmin(email);
        return ResponseEntity.ok(valid);
    }

    @GetMapping("/validateTeachersAdmins")
    public ResponseEntity<Boolean> validateTeachersAdmins(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean valid = userService.validateAdmin(email);
        boolean validteacher = userService.validateTeacher(email);
        return ResponseEntity.ok(valid || validteacher);
    }

    @PutMapping("/updateByEmail")
    public UserModel updateUserByEmail(@CookieValue(name = "JWT") String token, @RequestBody UserModel user) {
        String email = jwtService.extractEmailFromToken(token);
        if (email != null){
            activeUserService.addUserGeneral(email);
        }
        return userService.updateUserByEmail(email, user);
    }

    @GetMapping("/infoCompleteByEmail")
    public ResponseEntity<Boolean> infoCompleteByEmail(@CookieValue(name = "JWT") String token) {
        return ResponseEntity.ok(userService.InfoCompleteByEmail(jwtService.extractEmailFromToken(token)));
    }

    @GetMapping("/getInfoByEmail")
    public AuxiliarUserModel getInfoByEmail(@CookieValue(name = "JWT") String token) {
        return userService.getInfoByEmail(jwtService.extractEmailFromToken(token));
    }

    @GetMapping("/listUserInfo")
    public List<AuxiliarUserModel> getUsersWithSpecificFields(@CookieValue(name = "JWT") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return userService.obtainUserListWithSpecificFields();
        }
        return null;
    }

    @DeleteMapping("/deleteUserByEmail")
    public ResponseEntity<Boolean> deleteUserByEmail(
        @CookieValue(name = "JWT") String token, 
        @RequestHeader("user") String email) {

        try {
            if (!jwtService.ValidateTokenAdmin(token)) {
                return ResponseEntity.ok(false);
            }
            userService.deleteByEmail(email);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("/editRolByEmail")
    public ResponseEntity<Boolean> EditRolByEmail(
        @CookieValue(name = "JWT") String token, 
        @RequestHeader("user") String email,
        @RequestHeader("role") String newrole) {

        try {
            if (!jwtService.ValidateTokenAdmin(token)) {
                return ResponseEntity.ok(false);
            }
            userService.editRolByEmail(email, newrole);
            return ResponseEntity.ok(true);
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("/sendCustomMessage")
    public ResponseEntity<Boolean> sendCustomMessage(
        @CookieValue(name = "JWT") String token, 
        @RequestBody NotificationModel message) {

        try {
            if (!jwtService.ValidateTokenAdmin(token)) {
                return ResponseEntity.ok(false);
            }
            userService.sendCustomMessage(message.getEmail(), message.getTitle(), message.getMessage());
            return ResponseEntity.ok(true);
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
