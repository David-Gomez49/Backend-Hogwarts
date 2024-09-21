package com.api.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Classes")
public class ClassModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "id_group", referencedColumnName = "Id") // id es la columna en GroupModel
    private GroupModel group;

    @ManyToOne
    @JoinColumn(name = "id_teacher", referencedColumnName = "Id") // id es la columna en GroupModel
    private UserModel teacher;

    @ManyToOne
    @JoinColumn(name = "id_subject", referencedColumnName = "Id") // id es la columna en GroupModel
    private SubjectModel subject;

    private String schedule;

    public ClassModel() {
    }

    public ClassModel(int id, GroupModel group, UserModel teacher, SubjectModel subject, String schedule) {
        Id = id;
        this.group = group;
        this.teacher = teacher;
        this.subject = subject;
        this.schedule = schedule;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public UserModel getTeacher() {
        return teacher;
    }

    public void setTeacher(UserModel teacher) {
        this.teacher = teacher;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectModel subject) {
        this.subject = subject;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    
    
    
}
