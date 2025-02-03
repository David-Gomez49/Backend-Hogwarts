package com.api.backend.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name= "Users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "Id")
    private RolModel rol;
    
    private String name;
    private String lastname;
    private Date birthday;
    private String gender;
    private String address;
    private String phone;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String document_type;
    private String document_number;
    private String picture;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<UserxGroupModel> userGroups;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<AttendanceModel> attendances;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<CalificationModel> califications;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<StudentsXParentsModel> parentRelations;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<StudentsXParentsModel> studentRelations;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    private List<MessageModel> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<AlertModel> alerts;

    public UserModel() {
    }

    public UserModel(int id, String name, String lastname, Date birthday, String gender, String address, String phone,
            String email, String document_type, String document_number, RolModel rol, String picture) {
        Id = id;
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.document_type = document_type;
        this.document_number = document_number;
        this.rol = rol;
        this.picture = picture;
    }
    
    
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public RolModel getRol() {
        return rol;
    }

    public void setRol(RolModel rol) {
        this.rol = rol;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }
    
    
    
}
