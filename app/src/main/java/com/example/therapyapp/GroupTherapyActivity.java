package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyapp.RoomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupTherapyActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private ArrayList<Room> roomList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kullanıcı oturum açmış mı diye kontrol et
        checkUserAuthentication();
    }

    private void checkUserAuthentication() {
        // Firebase Authentication örneği al
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Kullanıcı oturum açmış mı kontrol et
        if (currentUser == null) {
            // Kullanıcı oturum açmamışsa, LoginActivity'e yönlendir
            startActivity(new Intent(GroupTherapyActivity.this, LoginActivity.class));
            // Bu aktiviteyi kapat
            finish();
        } else {
            // Kullanıcı oturum açmışsa, odaları listele
            initializeRooms();
        }
    }

    private void initializeRooms() {
        // Initialize roomList
        roomList = new ArrayList<>();

        // Fetch room data from Firestore
        db.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String roomId = document.getId();
                                int capacity = document.getLong("capacity").intValue();
                                int currentOccupancy = document.getLong("currentOccupancy").intValue();
                                roomList.add(new Room(roomId, capacity, currentOccupancy));
                            }

                            // Sort roomList based on occupancy
                            insertionSort(roomList);

                            // Initialize RecyclerView
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(GroupTherapyActivity.this));

                            // Initialize RoomAdapter and set it to RecyclerView
                            roomAdapter = new RoomAdapter(roomList, GroupTherapyActivity.this);
                            recyclerView.setAdapter(roomAdapter);
                        } else {
                            Toast.makeText(GroupTherapyActivity.this, "Error fetching rooms", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Insertion Sort algoritması
    public static void insertionSort(ArrayList<Room> rooms) {
        int n = rooms.size();
        for (int i = 1; i < n; ++i) {
            Room key = rooms.get(i);
            int j = i - 1;

            // Odaları doluluk oranına göre sırala
            while (j >= 0 && rooms.get(j).getCurrentOccupancy() < key.getCurrentOccupancy()) {
                rooms.set(j + 1, rooms.get(j));
                j = j - 1;
            }
            rooms.set(j + 1, key);
        }
    }

    // Odalara tıklandığında gerçekleşecek eylem
    @Override
    public void onRoomClick(String roomId) {
        // Tıklanan oda numarasına göre ilgili aktiviteye yönlendirme yapılabilir
        // Örneğin, tıklanan odaya göre farklı aktivitelere yönlendirme yapılabilir
        Intent intent = new Intent(GroupTherapyActivity.this, Room1Activity.class);
        intent.putExtra("roomId", roomId); // Pass the room ID as an extra
        startActivity(intent);
    }
}
