package com.api.backend.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Assesments")
public class AssesmentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_class", referencedColumnName = "Id") // id es la columna en GroupModel
    private ClassModel classes;

    private float percent;
    private String description;
    private LocalDate date;
    private LocalDate limit_date;

    public AssesmentModel() {
    }

    public AssesmentModel(int id, float percent, String description, LocalDate date, LocalDate limit_date, ClassModel classes) {
        this.Id = id;
        this.percent = percent;
        this.description = description;
        this.date = date;
        this.limit_date = limit_date;
        this.classes = classes;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public ClassModel getClasses() {
        return classes;
    }

    public void setClasses(ClassModel classes) {
        this.classes = classes;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getLimit_date() {
        return limit_date;
    }

    public void setLimit_date(LocalDate limit_date) {
        this.limit_date = limit_date;
    }
    
    
    

    
}
