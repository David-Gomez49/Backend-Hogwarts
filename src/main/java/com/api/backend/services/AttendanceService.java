package com.api.backend.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AttendanceModel;
import com.api.backend.model.ClassModel;
import com.api.backend.model.UserModel;
import com.api.backend.repository.AttendanceRepo;
import com.api.backend.repository.ClassRepo;
import com.api.backend.repository.UserXGroupRepo;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private UserXGroupRepo userXGroupRepo;

    public List<AttendanceModel> obtainAttendanceList() {
        return (List<AttendanceModel>) attendanceRepo.findAll();
    }

    public AttendanceModel createAttendance(AttendanceModel attendance) {
        return attendanceRepo.save(attendance);
    }

    public AttendanceModel updateAttendance(AttendanceModel attendance) {
        return attendanceRepo.save(attendance);
    }

    public void deleteAttendance(int id) {
        attendanceRepo.deleteById(id);
    }

    public List<AttendanceModel>  getAttendanceByClassId(int Id){
        return attendanceRepo.findAttendanceByclasses_Id(Id);
    }
    
    public List<AttendanceModel> obtainAttendancesByTeacher(String email) {
        List<ClassModel> classes = classRepo.findByTeacherEmail(email);
        List<AttendanceModel> allAttendances = new ArrayList<>();

        for (ClassModel classModel : classes) {
            List<AttendanceModel> attendances = attendanceRepo.findAttendanceByclasses_Id(classModel.getId());
            allAttendances.addAll(attendances);
        }
        return allAttendances;
    }


    public List<AttendanceModel> obtainAttendancesByStudent(String Email){
        return attendanceRepo.findAttendancesByStudent_Email(Email);
    }
    
   public List<AttendanceModel> saveAttendances(List<AttendanceModel> attendances) {
    List<AttendanceModel> savedAttendances = new ArrayList<>();
    
    for (AttendanceModel attendance : attendances) {
        // Verificar si la asistencia ya existe en la base de datos
        AttendanceModel existingAttendance = attendanceRepo.findByStudent_IdAndClasses_IdAndDate(
            attendance.getStudent().getId(), 
            attendance.getClasses().getId(), 
            attendance.getDate()
        );
        
        if (existingAttendance != null) {
            // Si la asistencia ya existe, actualizamos la información
            existingAttendance.setStatus(attendance.getStatus());
            savedAttendances.add(attendanceRepo.save(existingAttendance));
        } else {
            // Si no existe, creamos una nueva entrada
            savedAttendances.add(attendanceRepo.save(attendance));
        }
    }

    return savedAttendances;
}


public List<AttendanceModel> getAttendancesByClassIdAndDate(int classId, LocalDate date) {
    int groupId = classRepo.findById(classId).getGroup().getId();
    
    System.out.println("---------------------------");
    System.out.println("Original date: " + date);
    System.out.println("classId: " + classId);
    System.out.println("groupId: " + groupId);
    System.out.println("---------------------------");

    // Obtener los estudiantes de la clase (grupo)
    List<UserModel> studentsInClass = userXGroupRepo.findStudentsByGroupId(groupId);
    List<AttendanceModel> attendances = new ArrayList<>();

    // Itera sobre los estudiantes para obtener su asistencia en la fecha específica
    for (UserModel student : studentsInClass) {
        List<AttendanceModel> studentAttendance = attendanceRepo.getAttendancesByStudent_IdAndClasses_IdAndDate(student.getId(), classId, date);

        if (studentAttendance.isEmpty()) {
            System.out.println("No attendance found for student: " + student.getName());
            AttendanceModel emptyAttendance = new AttendanceModel();
            emptyAttendance.setStudent(student);
            emptyAttendance.setDate(date);
            
            // Asignar la clase correcta al objeto emptyAttendance
            ClassModel classModel = new ClassModel();
            classModel.setId(classId);
            emptyAttendance.setClasses(classModel);

            attendances.add(emptyAttendance);
        } else {
            System.out.println("Attendance found for student: " + student.getName());
            attendances.addAll(studentAttendance);
        }
    }

    return attendances;
}


}
