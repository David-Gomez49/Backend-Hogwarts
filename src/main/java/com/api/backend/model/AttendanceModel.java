package com.api.backend.model;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Attendances")
public class AttendanceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @ManyToOne
    @JoinColumn(name = "id_student", referencedColumnName = "Id") // id es la columna en GroupModel
    private UserModel student;

    @ManyToOne
    @JoinColumn(name = "id_class", referencedColumnName = "Id") // id es la columna en GroupModel
    private ClassModel classes;
    private LocalDate date;
    private String status;

    public AttendanceModel() {
    }

    public AttendanceModel(int id, UserModel student, ClassModel classes, LocalDate date, String status ) {
        Id = id;
        this.student = student;
        this.classes = classes;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public UserModel getStudent() {
        return student;
    }

    public void setStudent(UserModel student) {
        this.student = student;
    }

    public ClassModel getClasses() {
        return classes;
    }

    public void setClasses(ClassModel classes) {
        this.classes = classes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
