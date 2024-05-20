package com.example.therapyapp;

public class User {
    private String userId;
    private String name;
    private String disorder;
    private boolean userInRoom;

    // Boş yapıcı metot, Firestore için gereklidir.
    public User() {
    }

    public User(String userId, String name, String disorder, boolean inRoom) {
        this.userId = userId;
        this.name = name;
        this.disorder = disorder;
        this.userInRoom = inRoom;
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

    public boolean userInRoom() {
        return userInRoom;
    }

    public void setInRoom(boolean inRoom) {
        this.userInRoom = inRoom;
    }
}
