package com.api.backend.services;

import java.util.ArrayList;
import java.util.List;

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

    public List<CalificationModel> obtainCalificationList() {
        return (List<CalificationModel>) calificationRepo.findAll();
    }

    public List<CalificationModel> getCalificationsListByClass(int id) {
        int id_group=classRepo.findById(id).getGroup().getId();
        List<UserModel> studentsInClass = userXGroupRepo.findStudentsByGroupId(id_group); 
    
        List<CalificationModel> califications = new ArrayList<>();
    
        for (UserModel student : studentsInClass) {
            List<CalificationModel> studentCalifications = calificationRepo.findByStudent_IdAndAssesment_Classes_Id(student.getId(), id);
    
            if (studentCalifications.isEmpty()) {
                CalificationModel emptyCalification = new CalificationModel();
                emptyCalification.setStudent(student);  
                califications.add(emptyCalification);   
            } else {
                califications.addAll(studentCalifications);
            }
        }
    
        return califications;
    }
    

    public List<CalificationModel> getCalificationsByEmail(String email) {
    // Obtener todos los estudiantes
    UserModel student = userRepo.findByEmail(email); // Asumiendo que tienes un repositorio de estudiantes
    UserxGroupModel userGroup = userXGroupRepo.findByStudent(student);
    GroupModel group = new GroupModel();
    if (userGroup != null) {
        group = userGroup.getGroup();
    }
    else{
        return null;
    }
    List<ClassModel> classesList = classRepo.findByGroup(group);
    List<CalificationModel> califications = new ArrayList<>();

    // Para cada estudiante, obtener las calificaciones
    for (ClassModel classes : classesList) {
        
        List<CalificationModel> studentCalifications = calificationRepo.findByStudent_EmailAndAssesment_Classes_Id(email, classes.getId());

        // Si el estudiante no tiene calificaciones para esta clase, devolver una calificación vacía
        if (studentCalifications.isEmpty()) {
            CalificationModel emptyCalification = new CalificationModel();
            emptyCalification.setStudent(student);
            AssesmentModel emptyaAssesment = new AssesmentModel();
            emptyaAssesment.setClasses(classes); // Asignar el estudiante
            emptyCalification.setAssesment(emptyaAssesment); // Asignar la clase
            califications.add(emptyCalification);   // Añadir la calificación vacía a la lista
        } else {
            califications.addAll(studentCalifications);
        }
    }

    return califications;
}


public void saveOrUpdateCalifications(List<CalificationModel> califications) {
    for (CalificationModel calification : califications) {
        try {
            // Buscar calificación existente basada en estudiante y evaluación
            CalificationModel existingCalification = calificationRepo
                .findByStudent_EmailAndAssesment_Id(
                    calification.getStudent().getEmail(),
                    calification.getAssesment().getId()
                );

            if (existingCalification != null) {
                // Si existe, actualiza la calificación
                existingCalification.setCalification(calification.getCalification());
                calificationRepo.save(existingCalification);
            } else {
                // Si no existe, crea una nueva entrada
                calificationRepo.save(calification);
            }
        } catch (Exception e) {
            System.err.println("Error procesando calificación para el estudiante: " 
                + calification.getStudent().getEmail() 
                + " y evaluación: " + calification.getAssesment().getId());
            e.printStackTrace();
        }
    }
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
}
