package com.api.backend.model;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Messages")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "id_sender", referencedColumnName = "Id")
    private UserModel sender;
    @ManyToOne
    @JoinColumn(name = "id_reply", referencedColumnName = "Id")
    @JsonBackReference 
    private MessageModel reply;
    @ManyToOne
    @JoinColumn(name = "id_class", referencedColumnName = "Id")
    private ClassModel classes;
    private String content;
    private Instant send_date;

    public MessageModel() {
    }

    public MessageModel(int id, UserModel sender, MessageModel reply, ClassModel classes, String content, Instant send_date) {
        Id = id;
        this.sender = sender;
        this.reply = reply;
        this.classes = classes;
        this.content = content;
        this.send_date = send_date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public MessageModel getReply() {
        return reply;
    }

    public void setReply(MessageModel reply) {
        this.reply = reply;
    }

    public ClassModel getClasses() {
        return classes;
    }

    public void setClasses(ClassModel classes) {
        this.classes = classes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getSend_date() {
        return send_date;
    }

    public void setSend_date(Instant send_date) {
        this.send_date = send_date;
    }
    
}
