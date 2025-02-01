package com.api.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AssesmentModel;
import com.api.backend.model.CalificationModel;
import com.api.backend.model.ClassModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.repository.CalificationRepo;
import com.api.backend.repository.ClassRepo;
import com.api.backend.repository.UserRepo;
import com.api.backend.repository.UserXGroupRepo;

@Service
public class CalificationService {

    @Autowired
    private CalificationRepo calificationRepo;
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private UserXGroupRepo userXGroupRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AlertService alertService;

    public List<CalificationModel> obtainCalificationList() {
        return (List<CalificationModel>) calificationRepo.findAll();
    }

    public List<CalificationModel> getCalificationsListByClass(int classId) {
        int groupId = classRepo.findById(classId).getGroup().getId();
        List<UserModel> studentsInClass = userXGroupRepo.findStudentsByGroupId(groupId);

        List<CalificationModel> califications = new ArrayList<>();
        studentsInClass.forEach(student -> {
            List<CalificationModel> studentCalifications = calificationRepo.findByStudent_IdAndAssesment_Classes_Id(student.getId(), classId);

            if (studentCalifications.isEmpty()) {
                califications.add(createEmptyCalification(student, null));
            } else {
                califications.addAll(studentCalifications);
            }
        });

        return califications;
    }
    
    public Map<String, Object> getCalificationsSummary(String email) {
        UserModel student = userRepo.findByEmail(email);
        UserxGroupModel userGroup = userXGroupRepo.findByStudent(student);
        
        if (userGroup == null) {
            return Map.of("student", student.getName() + " " + student.getLastname(), "subjects", List.of());
        }
        
        GroupModel group = userGroup.getGroup();
        List<ClassModel> classesList = classRepo.findByGroup(group);
        
        Map<String, List<Double>> subjectGrades = new HashMap<>();
        Map<String, Integer> subjectIds = new HashMap<>();
        for (ClassModel classModel : classesList) {
            List<CalificationModel> studentCalifications = 
                Optional.ofNullable(calificationRepo.findByStudent_EmailAndAssesment_Classes_Id(email, classModel.getId()))
                        .orElse(Collections.emptyList());
            
            String subjectName = classModel.getSubject().getName();
            subjectGrades.putIfAbsent(subjectName, new ArrayList<>());
            subjectIds.putIfAbsent(subjectName, classModel.getSubject().getId());
            
            for (CalificationModel calification : studentCalifications) {
                subjectGrades.get(subjectName).add((double) calification.getCalification());
            }
        }
        
        List<Map<String, Object>> subjects = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : subjectGrades.entrySet()) {
            double average = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            subjects.add(Map.of("subject", entry.getKey(), "average", average, "id", subjectIds.get(entry.getKey())));
        }
        
        return Map.of("student", student, "subjects", subjects);
    }
    


    public List<CalificationModel> getCalificationsByEmail(String email) {
        UserModel student = userRepo.findByEmail(email);
        UserxGroupModel userGroup = userXGroupRepo.findByStudent(student);

        if (userGroup == null) {
            return List.of(); // Devuelve una lista vacía si el estudiante no pertenece a un grupo.
        }

        GroupModel group = userGroup.getGroup();
        List<ClassModel> classesList = classRepo.findByGroup(group);

        List<CalificationModel> califications = new ArrayList<>();
        classesList.forEach(classModel -> {
            List<CalificationModel> studentCalifications = calificationRepo.findByStudent_EmailAndAssesment_Classes_Id(email, classModel.getId());

            if (studentCalifications.isEmpty()) {
                AssesmentModel emptyAssesment = new AssesmentModel();
                emptyAssesment.setClasses(classModel);
                califications.add(createEmptyCalification(student, emptyAssesment));
            } else {
                califications.addAll(studentCalifications);
            }
        });

        return califications;
    }


    public void saveOrUpdateCalifications(List<CalificationModel> califications) { 
        califications.forEach(calification -> {
            try {
                CalificationModel existingCalification = calificationRepo.findByStudent_IdAndAssesment_Id(
                    calification.getStudent().getId(),
                    calification.getAssesment().getId()
                );
    
                if (existingCalification != null) {
                    existingCalification.setCalification(calification.getCalification()); // Actualizar calificación
                    calificationRepo.save(existingCalification);
                    if (existingCalification.getCalification() != calification.getCalification() ){
                        if(calification.getCalification()<3){
                            alertService.addCounter(calification.getStudent().getEmail(), calification.getAssesment().getClasses());
                        }
                    }
                    
                } else {
                    if(calification.getCalification()<3){
                        alertService.addCounter(calification.getStudent().getEmail(), calification.getAssesment().getClasses());
                    }
                    calificationRepo.save(calification);
                }
            } catch (Exception e) {
                System.err.println("Error procesando calificación para el estudiante: "
                        + calification.getStudent().getId() + " y evaluación: "
                        + calification.getAssesment().getId());
            }
        });
    }
    



    public CalificationModel createCalification(CalificationModel calification) {
        return calificationRepo.save(calification);
    }

    public CalificationModel updateCalification(CalificationModel calification) {
        return calificationRepo.save(calification);
    }

    public void deleteCalification(int id) {
        calificationRepo.deleteById(id);
    }

    private CalificationModel createEmptyCalification(UserModel student, AssesmentModel assesment) {
        CalificationModel emptyCalification = new CalificationModel();
        emptyCalification.setStudent(student);
        emptyCalification.setAssesment(assesment);
        return emptyCalification;
    }
}
