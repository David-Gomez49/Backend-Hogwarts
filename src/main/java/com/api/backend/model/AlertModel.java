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
@Table(name= "Alert")
public class AlertModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @ManyToOne
    @JoinColumn(name = "id_class", referencedColumnName = "Id") // id es la columna en GroupModel
    private ClassModel classes;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_user", referencedColumnName = "Id") // id es la columna en GroupModel
    private UserModel user;

    private int counter;

    public AlertModel(){

    }

    public AlertModel(int id, ClassModel classes, UserModel user, int counter) {
        Id = id;
        this.classes = classes;
        this.user = user;
        this.counter = (counter != 0) ? counter : 0;
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        this.counter = this.counter + 1;
    }

    public void resetCounter() {
        this.counter = 0;
    }

    

    

}
