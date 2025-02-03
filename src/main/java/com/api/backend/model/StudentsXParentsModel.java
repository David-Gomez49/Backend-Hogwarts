package com.api.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name= "StudentsXParents")
public class StudentsXParentsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_parent", referencedColumnName = "Id")
    private UserModel parent;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_student", referencedColumnName = "Id")
    private UserModel student;

    public StudentsXParentsModel() {
    }

    public StudentsXParentsModel(int id, UserModel parent, UserModel student) {
        Id = id;
        this.parent = parent;
        this.student = student;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public UserModel getParent() {
        return parent;
    }

    public void setParent(UserModel parent) {
        this.parent = parent;
    }

    public UserModel getStudent() {
        return student;
    }

    public void setStudent(UserModel student) {
        this.student = student;
    }

    
    
}
