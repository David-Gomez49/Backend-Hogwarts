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
import com.api.backend.services.ClassService;

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

    @Autowired
    private ClassService classService;

    public void addCounter(int studentId, ClassModel classes) throws MessagingException {
        UserModel user = userService.obtainUserById(studentId);
        Optional<AlertModel> alertOpt = alertRepo.findOptionalByUser_IdAndClasses_Id(user.getId(), classes.getId());

        if (alertOpt.isPresent()) {
            AlertModel alert = alertOpt.get();
            alert.incrementCounter();
            alertRepo.save(alert);
        } else {
            AlertModel newAlert = new AlertModel();
            newAlert.setUser(user);
            newAlert.setClasses(classes);
            newAlert.incrementCounter();
            alertRepo.save(newAlert);
        }
        validCounter(user.getEmail(), classes, 3);
    }

    public void resetCounter(String email, ClassModel classes) {
        UserModel user = userService.obtainUserByEmail(email);
        AlertModel alert = alertRepo.findByUser_IdAndClasses_Id(user.getId(), classes.getId());
        alert.resetCounter();
        alertRepo.save(alert);

    }

    public void validCounter(String email, ClassModel classes, int threshold) throws MessagingException {
        UserModel user = userService.obtainUserByEmail(email);
        AlertModel alert = alertRepo.findByUser_IdAndClasses_Id(user.getId(), classes.getId());
        if (alert.getCounter() >= threshold) {
            String studentName = user.getName() + " " + user.getLastname();
            ClassModel clase = classService.getClassById(classes.getId());
            String className = clase.getSubject().getName();

            List<StudentsXParentsModel> parents = studentXparentService.obtainParentList(email);

            for (var parent : parents) {
                emailService.sendEmail(parent.getParent().getEmail(), studentName, className);
            }

            resetCounter(email, classes);
        }
    }
}