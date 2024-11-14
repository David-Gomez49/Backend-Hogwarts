package com.api.backend.services;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AttendanceModel;
import com.api.backend.model.ClassModel;
import com.api.backend.repository.AttendanceRepo;
import com.api.backend.repository.ClassRepo;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private ClassRepo classRepo;

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
    
    public void saveAttendances(List<AttendanceModel> attendances) {
        attendanceRepo.saveAll(attendances);
    }
    
     public List<AttendanceModel> getAttendancesByClassIdAndDate(int classId, String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = new Date(format.parse(date).getTime());
        } catch (Exception e) {
            // Manejo de error si la fecha no se puede parsear
            e.printStackTrace();
        }
        return attendanceRepo.findByClasses_IdAndDate(classId, parsedDate);
    }
}
