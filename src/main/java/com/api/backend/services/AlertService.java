package com.api.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AlertModel;
import com.api.backend.model.ClassModel;
import com.api.backend.model.StudentsXParentsModel;
import com.api.backend.model.UserModel;
import com.api.backend.repository.AlertRepo;

import jakarta.mail.MessagingException;

@Service
public class AlertService {

    @Autowired
    private AlertRepo alertRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private StudentXParentService studentXparentService;
    
    public void addCounter(int studentId,ClassModel classes) throws MessagingException {
        UserModel user = userService.obtainUserById(studentId);
        Optional<AlertModel> alertOpt = alertRepo.findByUser_IdAndClasses_Id(user.getId(),classes.getId());
        
        if (alertOpt.isPresent()) {
            AlertModel alert = alertOpt.get();
            alertRepo.save(alert);
            alert.incrementCounter();
        } else {
            AlertModel newAlert = new AlertModel();
            newAlert.setUser(user);
            newAlert.setClasses(classes);
            newAlert.incrementCounter();
            alertRepo.save(newAlert);
        }
        validCounter(user.getEmail(),classes,3);
    }

    public void resetCounter(String email,ClassModel classes) {
        UserModel user= userService.obtainUserByEmail(email);
        Optional<AlertModel> alertOpt = alertRepo.findByUser_IdAndClasses_Id(user.getId(),classes.getId());
        alertOpt.ifPresent(alert -> {
            alert.resetCounter();
            alertRepo.save(alert);
        });
    }

    public void validCounter(String email,ClassModel classes, int threshold) throws MessagingException{
        UserModel user= userService.obtainUserByEmail(email);
        Optional<AlertModel> alertOpt = alertRepo.findByUser_IdAndClasses_Id(user.getId(),classes.getId());
        if (alertOpt.isPresent() && alertOpt.get().getCounter() > threshold) {
            UserModel student = userService.obtainUserByEmail(email);
            String studentName = student.getName()+" "+student.getLastname();
            String clase = classes.getSubject().getName();
            
            List<StudentsXParentsModel> parents = studentXparentService.obtainParentList(email);

            for (var parent : parents) {
                emailService.sendEmail(parent.getParent().getEmail(), studentName, clase);
            }

            resetCounter(email,classes);
        }
    }
}