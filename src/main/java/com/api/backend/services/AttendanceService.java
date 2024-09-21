package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AttendanceModel;
import com.api.backend.repository.AttendanceRepo;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;

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
}
