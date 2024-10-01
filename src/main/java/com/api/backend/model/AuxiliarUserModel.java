package com.api.backend.model;



public class AuxiliarUserModel {

    private RolModel rol;
    private String name;
    private String lastname;
    private String email;
    private String document_type;
    private String document_number;
    private String picture;

    public AuxiliarUserModel(){
    }

    public AuxiliarUserModel(String document_number, String document_type, String email, String lastname, String name, String picture, RolModel rol) {
        this.document_number = document_number;
        this.document_type = document_type;
        this.email = email;
        this.lastname = lastname;
        this.name = name;
        this.picture = picture;
        this.rol = rol;
    }

    public RolModel getRol() {
        return rol;
    }

    public void setRol(RolModel rol) {
        this.rol = rol;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    
}
