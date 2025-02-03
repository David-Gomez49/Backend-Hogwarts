package com.api.backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.CalificationModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.CalificationService;
import com.api.backend.services.StudentXParentService;
import com.api.backend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/calification")
public class CalificationControl {
    @Autowired
    private CalificationService calificationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentXParentService studentXParentService;

    @GetMapping("/getCalificationsListByClass")
    public List<CalificationModel> getCalificationsListByClass(@CookieValue(name = "JWT") String token, @RequestHeader("ClassId") int Id) {
        if(jwtService.ValidateTokenAdminTeacher(token)){
            return calificationService.getCalificationsListByClass(Id);
        }
        return null;
    }

    @GetMapping("/getCalificationsSummaryByEmail")
    public List<Map<String, Object>> getCalificationsSummaryByEmail(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();
        
        List<String> emails = new ArrayList<>();
        if ("Student".equals(rolName)) {
            emails.add(email);
        } else if ("Parent".equals(rolName)) {
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                emails.add(son.getStudent().getEmail());
            }
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (String studentEmail : emails) {
            result.add(calificationService.getCalificationsSummary(studentEmail));
        }
        return result;
    }

    @GetMapping("/getCalificationsByEmail")
    public List<CalificationModel> getCalificationsByEmail(@CookieValue(name = "JWT") String token) {
        String email = jwtService.extractEmailFromToken(token);
        String rolName = userService.GetRolByEmail(email).getName();

        if ("Student".equals(rolName)) {
            return calificationService.getCalificationsByEmail(email);
        }
        if ("Parent".equals(rolName)) {
            List<CalificationModel> califications = new ArrayList<>();
            List<StudentsXParentsModel> sons = studentXParentService.obtainSonsList(email);
            for (StudentsXParentsModel son : sons) {
                califications.addAll(calificationService.getCalificationsByEmail(son.getStudent().getEmail()));
            }
            return califications;
        }
        return null;
    }

    @PostMapping("/CreateUpdateCalifications")
    public ResponseEntity<Boolean> CreateUpdateCalifications(@CookieValue(name = "JWT") String token, @RequestBody List<CalificationModel> califications) {
        if(jwtService.ValidateTokenAdminTeacher(token)){
            try {
                calificationService.saveOrUpdateCalifications(califications);
                return ResponseEntity.ok(true);
            } catch (Exception e) {
                return ResponseEntity.ok(false);
            }
            }
        return ResponseEntity.ok(false); 
    }


    @GetMapping("/downloadCalifications")
    public void descargarExcel(@CookieValue(name = "JWT") String token,HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if(jwtService.ValidateTokenAdminTeacher(token)){
            String Email = jwtService.extractEmailFromToken(token);
            String rolName = userService.GetRolByEmail(Email).getName();
            boolean admin = "Admin".equals(rolName);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm"));
            String name = "notas - " + timestamp;
            response.setHeader("Content-Disposition", "attachment; filename="+name+".xlsx");
            calificationService.generarExcel(response,admin,Email);
            
        }
    }

}