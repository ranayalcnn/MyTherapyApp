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
    private OnRoomClickListener onRoomClickListener; // Butona tıklanma dinleyicisi

    // Constructor ile dinleyiciyi (listener) başlatma
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
        holder.roomId.setText("Oda " + room.getRoomId());
        holder.occupancy.setText("Doluluk Oranı: " + room.getCurrentOccupancy() + "/" + room.getCapacity());

        // Butona tıklama işlemini burada dinleyiciye (listener) bildirme
        holder.roomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRoomClickListener != null) {
                    onRoomClickListener.onRoomClick(room.getRoomId()); // Tıklanan oda numarasını döndürme
                    if (room.getCurrentOccupancy() < room.getCapacity()) { // Kapasiteye ulaşılmadıysa
                        // Eğer kullanıcı daha önce bu odada değilse
                        if (!room.isUserInRoom()) {
                            // Kullanıcı odaya giriş yaptığında occupancy bilgisini güncelleme
                            room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
                            // Kullanıcı odaya girdiğinde odanın içinde olduğunu işaretleme
                            room.setUserInRoom(true);
                            // Odaları doluluk oranına göre sırala
                            sortRooms();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomId;
        TextView occupancy;
        Button roomButton; // Buton tanımı

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomId = itemView.findViewById(R.id.roomId);
            occupancy = itemView.findViewById(R.id.occupancy);
            roomButton = itemView.findViewById(R.id.roomButton); // Butonun referansını al
        }
    }

    // Butona tıklama dinleyicisi (listener) arayüzü
    public interface OnRoomClickListener {
        void onRoomClick(String roomId);
    }

    // Odaları insertion sort algoritması kullanarak sıralama
    public void sortRooms() {
        int n = roomList.size();
        for (int i = 1; i < n; ++i) {
            Room key = roomList.get(i);
            int j = i - 1;

            /* Odaları doluluk oranına göre sırala */
            while (j >= 0 && roomList.get(j).getCurrentOccupancy() < key.getCurrentOccupancy()) {
                roomList.set(j + 1, roomList.get(j));
                j = j - 1;
            }
            roomList.set(j + 1, key);
        }
        notifyDataSetChanged();
    }
}
