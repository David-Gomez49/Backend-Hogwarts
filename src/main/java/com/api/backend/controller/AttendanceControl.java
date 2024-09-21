package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.AttendanceModel;
import com.api.backend.services.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceControl {
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getAll")
    public List<AttendanceModel> obtainAttendanceList() {
        return attendanceService.obtainAttendanceList();
    }

    @PostMapping("/create")
    public AttendanceModel createAttendance(@RequestBody AttendanceModel attendance) {
        return attendanceService.createAttendance(attendance);
    }

    @PutMapping("/edit/{id}")
    public AttendanceModel updateAttendance(@PathVariable int id, @RequestBody AttendanceModel attendance) {
        attendance.setId(id);
        return attendanceService.updateAttendance(attendance);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAttendance(@PathVariable int id) {
        attendanceService.deleteAttendance(id);
    }
}

