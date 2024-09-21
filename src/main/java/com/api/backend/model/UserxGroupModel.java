package com.api.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
@Table(name= "UsersxGroups")
public class UserxGroupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @OneToOne
    @JoinColumn(name = "id_studeent", referencedColumnName = "Id")
    private UserModel student;

    @ManyToOne
    @JoinColumn(name = "id_group", referencedColumnName = "Id")
    private GroupModel group;

    public UserxGroupModel() {
    }

    public UserxGroupModel(int id, UserModel student, GroupModel group) {
        Id = id;
        this.student = student;
        this.group = group;
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

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    
}
