package com.api.backend.model;

public class NotificationModel {
    private String email;
    private String title;
    private String message;

    public NotificationModel() {
    }

    public NotificationModel(String email, String title, String message) {
        this.email = email;
        this.title = title;
        this.message = message;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
