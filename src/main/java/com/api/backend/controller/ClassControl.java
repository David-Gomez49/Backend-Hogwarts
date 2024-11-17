package com.api.backend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.api.backend.model.ClassModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.ClassService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;
import com.api.backend.services.UserXGroupService;

@RestController
@RequestMapping("/class")
public class ClassControl {

    @Autowired
    private ClassService classService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentXParentService studentXParentService;

    @Autowired
    private UserXGroupService userGroupService;

    @GetMapping("/getAll")
    public List<ClassModel> obtainClassList(@CookieValue(name = "JSESSIONID") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return classService.obtainClassList();
        }
        return null;
    }

    @GetMapping("/getClassById")
    public ClassModel obtainClassList(@CookieValue(name = "JSESSIONID") String token, @RequestHeader("ClassId") int Id) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();
        ClassModel classes = classService.getClassById(Id);
        if (jwtService.ValidateTokenAdminTeacher(token)) {
            return classes;
        }

        if ("Student".equals(rolName)) {
            UserxGroupModel userGroup = userGroupService.findByStudent(userService.obtainUserByEmail(email));
            if (userGroup != null && userGroup.getGroup().equals(classes.getGroup())) {
                return classes;
            }
        }
        if ("Parent".equals(rolName)) {
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                UserxGroupModel userGroup = userGroupService.findByStudent(son.getStudent());
                if (userGroup != null && userGroup.getGroup().equals(classes.getGroup())) {
                    return classes;
                }
            }
        }
        return null;
    }

    @GetMapping("/getMyClasses")
    public List<ClassModel> obtainMyClassList(@CookieValue(name = "JSESSIONID") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();

        if ((rolName.equals("Admin"))) {
            return classService.obtainClassList();
        }
        if ((rolName.equals("Teacher"))) {
            return classService.obtainClassByTeacher(email);
        }
        if (rolName.equals("Student")) {
            UserxGroupModel userGroup = userGroupService.findByStudent(userService.obtainUserByEmail(email));
            if (userGroup != null) {
                GroupModel group = userGroup.getGroup();
                return classService.obtainClassByGroup(group);
            }
        }
        if ("Parent".equals(rolName)) {
            Set<ClassModel> classes = new HashSet<>(); // Cambiamos a Set para evitar duplicados
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);

            for (StudentsXParentsModel son : sons) {
                UserxGroupModel userGroup = userGroupService.findByStudent(son.getStudent());
                if (userGroup != null) {
                    GroupModel group = userGroup.getGroup();
                    List<ClassModel> classesByGroup = classService.obtainClassByGroup(group);
                    classes.addAll(classesByGroup); // HashSet se encarga de evitar duplicados
                }
            }
            return new ArrayList<>(classes); // Convertimos el Set a List si es necesario para el retorno
    }
    return null;

    }@PostMapping("/create")
    public ResponseEntity<Boolean> createClass(@CookieValue(name = "JSESSIONID") String token, @RequestBody ClassModel classes) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                classService.createClass(classes);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateClass(@CookieValue(name = "JSESSIONID") String token, @RequestBody ClassModel classes) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                classService.updateClass(classes);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteClass(@CookieValue(name = "JSESSIONID") String token, @RequestHeader("id") int id) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                classService.deleteClass(id);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }
}