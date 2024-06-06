package com.example.therapyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private ArrayList<Room> roomList;
    private OnRoomClickListener onRoomClickListener;

    // Constructor to initialize the listener
    public RoomAdapter(ArrayList<Room> roomList, OnRoomClickListener onRoomClickListener) {
        this.roomList = roomList;
        this.onRoomClickListener = onRoomClickListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomId.setText("Room " + room.getRoomId());
        holder.occupancy.setText("Occupancy: " + room.getCurrentOccupancy() + "/" + room.getCapacity());

        holder.roomButton.setOnClickListener(v -> {
            if (onRoomClickListener != null) {
                onRoomClickListener.onRoomClick(room.getRoomId());
                handleRoomClick(room);
            }
        });
    }

    // Handle room click event
    private void handleRoomClick(Room room) {
        if (room.getCurrentOccupancy() < room.getCapacity()) {
            if (!room.isUserInRoom()) {
                room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
                room.setUserInRoom(true);
                sortRooms();
            }
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomId;
        TextView occupancy;
        Button roomButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomId = itemView.findViewById(R.id.roomId);
            occupancy = itemView.findViewById(R.id.occupancy);
            roomButton = itemView.findViewById(R.id.roomButton);
        }
    }

    // Interface for room click listener
    public interface OnRoomClickListener {
        void onRoomClick(int roomId);
    }

    // Sort rooms by occupancy using Comparator
    public void sortRooms() {
        roomList.sort((room1, room2) -> Integer.compare(room2.getCurrentOccupancy(), room1.getCurrentOccupancy()));
        notifyDataSetChanged();
    }
}
