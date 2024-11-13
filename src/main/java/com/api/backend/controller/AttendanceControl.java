package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.backend.services.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceControl {
    @Autowired
    private AttendanceService attendanceService;

}

