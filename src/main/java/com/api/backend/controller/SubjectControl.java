package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.services.SubjectService;

@RestController
@RequestMapping("/subject")
public class SubjectControl {
    @Autowired
    private SubjectService subjectService;

}
