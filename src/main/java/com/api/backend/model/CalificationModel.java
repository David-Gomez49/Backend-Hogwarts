package com.api.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Califications")
public class CalificationModel{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float calification;
    @ManyToOne
    @JoinColumn(name = "id_Student", referencedColumnName = "Id")
    private UserModel student;
    @ManyToOne
    @JoinColumn(name = "id_assesment", referencedColumnName = "Id")
    private AssesmentModel assesment;
    private Boolean state;

    public CalificationModel() {
    }
    public CalificationModel(int id, float calification, UserModel student, AssesmentModel assesment, Boolean state) {
        this.id = id;
        this.calification = calification;
        this.student = student;
        this.assesment = assesment;
        this.state = state;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getCalification() {
        return calification;
    }
    public void setCalification(float calification) {
        this.calification = calification;
    }
    public UserModel getStudent() {
        return student;
    }
    public void setStudent(UserModel student) {
        this.student = student;
    }
    public AssesmentModel getAssesment() {
        return assesment;
    }
    public void setAssesment(AssesmentModel assesment) {
        this.assesment = assesment;
    }
    public Boolean isState() {
        return state;
    }
    public void setState(Boolean state) {
        this.state = state;
    }

    
}

