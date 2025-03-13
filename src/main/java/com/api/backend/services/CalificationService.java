package com.api.backend.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import jakarta.servlet.http.HttpServletResponse;

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
    @Autowired
    private ClassService classService;
    @Autowired
    private AssesmentService assesmentService;

    public List<CalificationModel> obtainCalificationList() {
        return (List<CalificationModel>) calificationRepo.findAll();
    }

    public List<CalificationModel> getCalificationsListByClass(int classId) {
        int groupId = classRepo.findById(classId).getGroup().getId();
        List<UserModel> studentsInClass = userXGroupRepo.findStudentsByGroupId(groupId);

        List<CalificationModel> califications = new ArrayList<>();
        studentsInClass.forEach(student -> {
            List<CalificationModel> studentCalifications = calificationRepo
                    .findByStudent_IdAndAssesment_Classes_Id(student.getId(), classId);

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

    Map<String, Double> weightedSum = new HashMap<>();
    Map<String, Double> totalWeight = new HashMap<>();
    Map<String, Integer> subjectIds = new HashMap<>();

    for (ClassModel classModel : classesList) {
        List<CalificationModel> studentCalifications = Optional
                .ofNullable(calificationRepo.findByStudent_EmailAndAssesment_Classes_Id(email, classModel.getId()))
                .orElse(Collections.emptyList());

        String subjectName = classModel.getSubject().getName();
        weightedSum.putIfAbsent(subjectName, 0.0);
        totalWeight.putIfAbsent(subjectName, 0.0);
        subjectIds.putIfAbsent(subjectName, classModel.getSubject().getId());

        for (CalificationModel calification : studentCalifications) {
            double grade = calification.getCalification();
            double weight = calification.getAssesment().getPercent();  // Se asume que existe este campo
            weightedSum.put(subjectName, weightedSum.get(subjectName) + (grade * weight));
            totalWeight.put(subjectName, totalWeight.get(subjectName) + weight);
        }
    }

    List<Map<String, Object>> subjects = new ArrayList<>();
    for (String subject : weightedSum.keySet()) {
        double average = totalWeight.get(subject) != 0 
                        ? weightedSum.get(subject) / totalWeight.get(subject)
                        : 0.0;
        subjects.add(Map.of("subject", subject, "average", average, "id", subjectIds.get(subject)));
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
            List<CalificationModel> studentCalifications = calificationRepo
                    .findByStudent_EmailAndAssesment_Classes_Id(email, classModel.getId());

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
    ClassModel clase = assesmentService.obtainAssestmentsById(califications.get(0).getAssesment().getId()).getClasses();
    califications.forEach(calification -> {
        try {
            CalificationModel existingCalification = calificationRepo.findByStudent_IdAndAssesment_Id(
                calification.getStudent().getId(),
                calification.getAssesment().getId()
            );

            if (existingCalification != null) {
                float oldCalification = existingCalification.getCalification();
                float newCalification = calification.getCalification();
                
                existingCalification.setCalification(newCalification); // Actualizar calificación
                calificationRepo.save(existingCalification);
                
                if (Float.compare(oldCalification, newCalification) != 0) {

                    if (newCalification < 3) {
                        alertService.addCounter(calification.getStudent().getId(), clase);
                    }
                }

            } else {
                calificationRepo.save(calification);

                if (calification.getCalification() < 3) {
                    alertService.addCounter(calification.getStudent().getId(), clase);
                }
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



    public void generarExcel(HttpServletResponse response, boolean Admin, String Email) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        List<CalificationModel> allDatos = Admin 
            ? calificationRepo.findAll() 
            : calificationRepo.findByAssesment_Classes_Teacher_Email(Email);

        Map<String, List<CalificationModel>> datos = allDatos.stream()
            .collect(Collectors.groupingBy(nota -> 
                nota.getAssesment().getClasses().getGroup().getGrade() + 
                "-" + nota.getAssesment().getClasses().getGroup().getVariant()
            ));

        for (String grupo : datos.keySet()) {
            Sheet sheet = workbook.createSheet("Grupo " + grupo);
            int rowNum = 0;

            Map<String, List<CalificationModel>> materias = datos.get(grupo).stream()
                .collect(Collectors.groupingBy(nota -> 
                    nota.getAssesment().getClasses().getSubject().getName()
                ));

            for (String materia : materias.keySet()) {
                rowNum += 2;
                Row materiaRow = sheet.createRow(rowNum++);
                materiaRow.createCell(0).setCellValue("Materia: " + materia);

                Row descripcionRow = sheet.createRow(rowNum++);
                Row porcentajeRow = sheet.createRow(rowNum++);
                porcentajeRow.createCell(0).setCellValue("Porcentaje");

                List<CalificationModel> notasMateria = materias.get(materia);

                List<String> evaluaciones = notasMateria.stream()
                    .map(nota -> nota.getAssesment().getDescription())
                    .distinct()
                    .collect(Collectors.toList());

                int colIndex = 1;
                for (String evaluacion : evaluaciones) {
                    descripcionRow.createCell(colIndex).setCellValue(evaluacion);
                    porcentajeRow.createCell(colIndex).setCellValue(
                        notasMateria.stream()
                            .filter(nota -> nota.getAssesment().getDescription().equals(evaluacion))
                            .findFirst().get().getAssesment().getPercent()
                    );
                    colIndex++;
                }
                descripcionRow.createCell(colIndex).setCellValue("Total Ponderado");

                Map<String, List<CalificationModel>> notasPorEstudiante = notasMateria.stream()
                    .collect(Collectors.groupingBy(nota -> nota.getStudent().getName()));

                for (String estudiante : notasPorEstudiante.keySet()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(estudiante);

                    colIndex = 1;
                    double totalPonderado = 0;

                    for (String evaluacion : evaluaciones) {
                        double notaValor = notasPorEstudiante.get(estudiante).stream()
                            .filter(nota -> nota.getAssesment().getDescription().equals(evaluacion))
                            .mapToDouble(CalificationModel::getCalification)
                            .findFirst().orElse(0.0);

                        row.createCell(colIndex).setCellValue(notaValor);
                        totalPonderado += notaValor * notasPorEstudiante.get(estudiante).stream()
                            .filter(nota -> nota.getAssesment().getDescription().equals(evaluacion))
                            .findFirst().get().getAssesment().getPercent() / 100;
                        colIndex++;
                    }

                    row.createCell(colIndex).setCellValue(totalPonderado);
                }
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
