package com.example.therapyapp;


public class Room {
    private int roomId;
    private int capacity;
    private int currentOccupancy;
    private boolean userInRoom; // Kullanıcının odada olup olmadığını belirten boolean değeri

    public Room(int roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.currentOccupancy = 0;
        this.userInRoom = false; // Başlangıçta hiçbir kullanıcı odada olmadığı için false olarak ayarla
    }

    public int getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public boolean isUserInRoom() {
        return userInRoom;
    }

    public void setUserInRoom(boolean userInRoom) {
        this.userInRoom = userInRoom;
    }
}