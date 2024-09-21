package com.api.backend.model;

import java.sql.Date;

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
    private Date date;
    private Boolean status;
    private String description;

    public AttendanceModel() {
    }

    public AttendanceModel(int id, UserModel student, ClassModel classes, Date date, Boolean status, String description) {
        Id = id;
        this.student = student;
        this.classes = classes;
        this.date = date;
        this.status = status;
        this.description = description;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    
    
    
    
    
}
