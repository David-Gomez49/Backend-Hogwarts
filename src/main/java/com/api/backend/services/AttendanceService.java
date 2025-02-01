package com.api.backend.services;

import java.time.LocalDate;
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

import jakarta.mail.MessagingException;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private UserXGroupRepo userXGroupRepo;
    @Autowired
    private AlertService alertService;

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
        classes.forEach(classModel -> 
            allAttendances.addAll(attendanceRepo.findAttendanceByclasses_Id(classModel.getId()))
        );
        return allAttendances;
    }


    public List<AttendanceModel> obtainAttendancesByStudent(String Email){
        return attendanceRepo.findAttendancesByStudent_Email(Email);
    }
    
    public void saveAttendances(List<AttendanceModel> attendances) throws MessagingException {
        List<AttendanceModel> savedAttendances = new ArrayList<>();
        attendances.forEach(attendance -> {
            AttendanceModel existingAttendance = attendanceRepo.findByStudent_IdAndClasses_IdAndDate(
                attendance.getStudent().getId(),
                attendance.getClasses().getId(),
                attendance.getDate()
            );
    
            if (existingAttendance != null) {
                existingAttendance.setStatus(attendance.getStatus());
                savedAttendances.add(attendanceRepo.save(existingAttendance));
    
                if (!existingAttendance.getStatus().equals(attendance.getStatus())) {
                    if(attendance.getStatus().equals("ausente")){
                        try {
                            alertService.addCounter(attendance.getStudent().getId(), attendance.getClasses());
                        } catch (MessagingException e) {
                            // Maneja el error aquí
                            e.printStackTrace(); // O cualquier otra forma de manejarlo
                        }
                    }
                }
            } else {
                savedAttendances.add(attendanceRepo.save(attendance));
    
                if(attendance.getStatus().equals("ausente")){
                    try {
                        alertService.addCounter(attendance.getStudent().getId(), attendance.getClasses());
                    } catch (MessagingException e) {
                        // Maneja el error aquí
                        e.printStackTrace(); // O cualquier otra forma de manejarlo
                    }
                }
            }
        });
    }
    


    public List<AttendanceModel> getAttendancesByClassIdAndDate(int classId, LocalDate date) {
        int groupId = classRepo.findById(classId).getGroup().getId();
        List<UserModel> studentsInClass = userXGroupRepo.findStudentsByGroupId(groupId);
        List<AttendanceModel> attendances = new ArrayList<>();

        studentsInClass.forEach(student -> {
            List<AttendanceModel> studentAttendances = attendanceRepo.getAttendancesByStudent_IdAndClasses_IdAndDate(
                student.getId(), classId, date
            );

            if (studentAttendances.isEmpty()) {
                AttendanceModel emptyAttendance = createEmptyAttendance(student, classId, date);
                attendances.add(emptyAttendance);
            } else {
                attendances.addAll(studentAttendances);
            }
        });

        return attendances;
    }

    private AttendanceModel createEmptyAttendance(UserModel student, int classId, LocalDate date) {
        AttendanceModel emptyAttendance = new AttendanceModel();
        emptyAttendance.setStudent(student);
        emptyAttendance.setDate(date);

        ClassModel classModel = new ClassModel();
        classModel.setId(classId);
        emptyAttendance.setClasses(classModel);

        return emptyAttendance;
    }


}
