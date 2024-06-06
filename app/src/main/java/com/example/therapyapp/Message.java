package com.example.therapyapp;

public class Message {
    private String senderId;
    private String message;
    private long timestamp;

    // Default constructor for Firestore
    public Message() {}

    // Parameterized constructor to initialize message properties
    public Message(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter for senderId
    public String getSenderId() {
        return senderId;
    }

    // Setter for senderId
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
