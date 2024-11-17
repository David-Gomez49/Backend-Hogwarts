package com.api.backend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AttendanceModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.AttendanceService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;

@RestController
@RequestMapping("/attendance")
public class AttendanceControl {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentXParentService studentXParentService;

    @GetMapping("/getAll")
    public List<AttendanceModel> obtainAttendancesList(@CookieValue(name = "JSESSIONID") String token) {
        if (jwtService.ValidateTokenAdmin(token)) {
            return attendanceService.obtainAttendanceList();
        }
        return null;
    }

    @GetMapping("/getAttendancesByClassId")
    public List<AttendanceModel> getAttendancesByClassId(@CookieValue(name = "JSESSIONID") String token, @RequestHeader("ClassId") int Id) {
       
        if (jwtService.ValidateTokenAdminTeacher(token)) {
            return attendanceService.getAttendanceByClassId(Id);
        }
        return null;
    }

    @GetMapping("/getAttendancesByClassId_Date")
    public List<AttendanceModel> getAttendancesByClassId_Date(@CookieValue(name = "JSESSIONID") String token, @RequestHeader("ClassId") int Id, @RequestHeader("SelectedDate") String SelectedDate) {       
        if (jwtService.ValidateTokenAdminTeacher(token)) {
            LocalDate date = LocalDate.parse(SelectedDate);
            return attendanceService.getAttendancesByClassIdAndDate(Id, date);
        }
        return null;
    }

    @GetMapping("/getMyAttendances")
    public List<AttendanceModel> obtainMyClassList(@CookieValue(name = "JSESSIONID") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();
        if ((rolName.equals("Admin"))) {
            return attendanceService.obtainAttendanceList();
        }
        if ((rolName.equals("Teacher"))) {
            return attendanceService.obtainAttendancesByTeacher(email);
        }
        if (rolName.equals("Student")) {
            return attendanceService.obtainAttendancesByStudent(email);
        }
        if ("Parent".equals(rolName)) {
            List<AttendanceModel> attendances = new ArrayList<>(); // Cambiamos a Set para evitar duplicados
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                attendances.addAll(attendanceService.obtainAttendancesByStudent(son.getStudent().getEmail())); // HashSet se encarga de evitar duplicados
            }
            return new ArrayList<>(attendances); // Convertimos el Set a List si es necesario para el retorno
    }
    return null;

    }

    @PostMapping("/createAttendances")
     public ResponseEntity<Boolean> createAttendance(@CookieValue(name = "JSESSIONID") String token, @RequestBody List<AttendanceModel> attendances) {
        try {
            if (jwtService.ValidateTokenAdminTeacher(token)) {
            attendanceService.saveAttendances(attendances);
            return ResponseEntity.ok(true);}
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }return ResponseEntity.ok(false);
    }

}

