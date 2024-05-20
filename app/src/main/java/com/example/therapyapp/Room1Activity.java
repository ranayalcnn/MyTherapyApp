package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.List;

public class Room1Activity extends AppCompatActivity {
    private int roomId;
    private List<User> userList; // Odadaki kullanıcıların listesi
    private TextView occupancyTextView; // Doluluk oranını gösteren TextView
    private FirebaseFirestore firestore;
    private ListenerRegistration userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Firestore başlat
        firestore = FirebaseFirestore.getInstance();

        // Intent'ten roomId al
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", -1);

        // roomId'ye göre sayfayı ayarla
        TextView roomTextView = findViewById(R.id.roomTextView);
        roomTextView.setText("Oda " + roomId);

        // Doluluk oranını gösteren TextView'i bul
        occupancyTextView = findViewById(R.id.occupancyTextView);

        // Odadaki kullanıcı listesini oluştur
        userList = new ArrayList<>();

        // Firestore'dan kullanıcıları dinleme
        listenForUsers();
    }

    // Firestore'dan kullanıcıları dinlemek için
    private void listenForUsers() {
        userListener = firestore.collection("rooms").document(String.valueOf(roomId)).collection("users")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Hata varsa işleme gerekirse burada ele alınabilir
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    // Kullanıcı eklendiğinde
                                    User user = dc.getDocument().toObject(User.class);
                                    userEnteredRoom(user);
                                    break;
                                case REMOVED:
                                    // Kullanıcı kaldırıldığında
                                    User removedUser = dc.getDocument().toObject(User.class);
                                    userLeftRoom(removedUser);
                                    break;
                                // Diğer durumlar için gerekirse işlemler eklenebilir
                            }
                        }
                    }
                });
    }

    // Kullanıcı odaya girdiğinde çağrılır
    private void userEnteredRoom(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
            // Doluluk oranını güncelle
            updateOccupancy();
        }
    }

    // Kullanıcı odayı terk ettiğinde çağrılır
    private void userLeftRoom(User user) {
        if (userList.contains(user)) {
            userList.remove(user);
            // Doluluk oranını güncelle
            updateOccupancy();
        }
    }

    // Doluluk oranını güncelle
    private void updateOccupancy() {
        int occupancy = userList.size(); // Odadaki kullanıcı sayısı
        occupancyTextView.setText("Doluluk Oranı: " + occupancy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Listener'ı kaldır
        if (userListener != null) {
            userListener.remove();
        }
    }
}
