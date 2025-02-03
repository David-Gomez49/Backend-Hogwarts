package com.api.backend.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

        Map<String, List<Double>> subjectGrades = new HashMap<>();
        Map<String, Integer> subjectIds = new HashMap<>();
        for (ClassModel classModel : classesList) {
            List<CalificationModel> studentCalifications = Optional
                    .ofNullable(calificationRepo.findByStudent_EmailAndAssesment_Classes_Id(email, classModel.getId()))
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
        List<CalificationModel> allDatos = null;
        
        if (Admin) {
            allDatos = calificationRepo.findAll();
        } else {
            allDatos = calificationRepo.findByAssesment_Classes_Teacher_Email(Email);
        }
        
        // Agrupar calificaciones por grupo
        Map<String, List<CalificationModel>> datos = allDatos
                .stream()
                .collect(Collectors.groupingBy(nota -> nota.getAssesment().getClasses().getGroup().getGrade() + "-" 
                        + nota.getAssesment().getClasses().getGroup().getVariant()));
    
        // Crear una hoja por cada grupo
        for (String grupo : datos.keySet()) {
            Sheet sheet = workbook.createSheet("Grupo " + grupo);
            int rowNum = 0;
    
            // Agrupar notas por materia dentro del grupo
            Map<String, List<CalificationModel>> materias = datos.get(grupo)
                    .stream()
                    .collect(Collectors.groupingBy(nota -> nota.getAssesment().getClasses().getSubject().getName()));
    
            for (String materia : materias.keySet()) {
                // Dejar 2 filas vacías antes de cada materia
                rowNum += 2;
    
                // Agregar título de materia
                Row materiaRow = sheet.createRow(rowNum++);
                materiaRow.createCell(0).setCellValue("Materia: " + materia);
    
                // Crear fila de descripciones y porcentajes
                Row descripcionRow = sheet.createRow(rowNum++);
                Row porcentajeRow = sheet.createRow(rowNum++);
                porcentajeRow.createCell(0).setCellValue("Porcentaje");
    
                // Obtener evaluaciones únicas
                List<CalificationModel> notasMateria = materias.get(materia);
                List<String> evaluaciones = notasMateria.stream()
                        .map(nota -> nota.getAssesment().getDescription())  // Usamos el nombre de la evaluación
                        .distinct()
                        .collect(Collectors.toList());
    
                int colIndex = 1;
                for (String evaluacion : evaluaciones) {
                    descripcionRow.createCell(colIndex).setCellValue(evaluacion);
                    
                    // Obtener el porcentaje de cada evaluación individualmente
                    Optional<CalificationModel> notaPorEvaluacion = notasMateria.stream()
                            .filter(n -> n.getAssesment().getDescription().equals(evaluacion))
                            .findFirst();
                    
                    if (notaPorEvaluacion.isPresent()) {
                        porcentajeRow.createCell(colIndex).setCellValue(notaPorEvaluacion.get().getAssesment().getPercent());
                    }
                    colIndex++;
                }
                descripcionRow.createCell(colIndex).setCellValue("Total Ponderado");
    
                // Mantener un registro de los estudiantes ya procesados para evitar duplicados
                Set<String> estudiantesProcesados = new HashSet<>();
    
                // Insertar notas de estudiantes
                for (CalificationModel nota : notasMateria) {
                    String estudianteNombre = nota.getStudent().getName();
    
                    // Si el estudiante ya fue procesado, continuar sin agregar una nueva fila
                    if (estudiantesProcesados.contains(estudianteNombre)) {
                        continue;
                    }
    
                    // Si no, agregar una nueva fila para el estudiante
                    estudiantesProcesados.add(estudianteNombre);
    
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(estudianteNombre);
    
                    colIndex = 1;
                    double totalPonderado = 0;
                    for (String evaluacion : evaluaciones) {
                        // Filtrar las notas del estudiante para esa evaluación en particular
                        Optional<CalificationModel> notaEstudiante = notasMateria.stream()
                                .filter(n -> n.getStudent().getName().equals(estudianteNombre) && n.getAssesment().getDescription().equals(evaluacion))
                                .findFirst();
    
                        if (notaEstudiante.isPresent()) {
                            double notaValor = notaEstudiante.get().getCalification();
                            row.createCell(colIndex).setCellValue(notaValor);
                            totalPonderado += notaValor * notaEstudiante.get().getAssesment().getPercent() / 100;
                        } else {
                            row.createCell(colIndex).setCellValue(0); // Si no hay calificación, asignar 0
                        }
                        colIndex++;
                    }
    
                    row.createCell(colIndex).setCellValue(totalPonderado);
                }
            }
        }
    
        // Escribir el archivo en la respuesta HTTP
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    

}
