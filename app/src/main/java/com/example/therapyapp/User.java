package com.example.therapyapp;

public class User {
    private String userId;
    private String name;
    private String disorder;

    // Empty constructor required for Firestore
    public User() {
    }

    public User(String userId, String name, String disorder) {
        this.userId = userId;
        this.name = name;
        this.disorder = disorder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisorder() {
        return disorder;
    }

    public void setDisorder(String disorder) {
        this.disorder = disorder;
    }
}
