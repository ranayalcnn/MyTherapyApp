package com.example.therapyapp;

public class Room {
    private int roomId;
    private int capacity;
    private int currentOccupancy;
    private boolean userInRoom; // Boolean value indicating whether the user is in the room

    // Constructor to initialize room ID and capacity
    public Room(int roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.currentOccupancy = 0;
        this.userInRoom = false; // Initially set to false as no user is in the room
    }

    // Getter for room ID
    public int getRoomId() {
        return roomId;
    }

    // Getter for room capacity
    public int getCapacity() {
        return capacity;
    }

    // Getter for current occupancy
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    // Setter for current occupancy
    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    // Getter for user in room status
    public boolean isUserInRoom() {
        return userInRoom;
    }

    // Setter for user in room status
    public void setUserInRoom(boolean userInRoom) {
        this.userInRoom = userInRoom;
    }
}
