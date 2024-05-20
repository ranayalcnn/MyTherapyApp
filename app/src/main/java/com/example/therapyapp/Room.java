package com.example.therapyapp;

import com.google.firebase.firestore.DocumentSnapshot;

public class Room {
    private String roomId;
    private int capacity;
    private int currentOccupancy;
    private boolean userInRoom;

    public Room(String roomId, int capacity, int currentOccupancy) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.currentOccupancy = currentOccupancy;
        this.userInRoom = userInRoom;
    }

    public Room(int roomId, int capacity, int currentOccupancy, boolean userInRoom) {
        this(String.valueOf(roomId), capacity, currentOccupancy);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    // Firestore belgesinden bir Room nesnesi oluştur
    public static Room fromSnapshot(DocumentSnapshot snapshot) {
        Room room = snapshot.toObject(Room.class);
        if (room != null) {
            room.setRoomId(snapshot.getId());
        }
        return room;
    }
}
