package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

public class GroupTherapyActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private ArrayList<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Check if user is authenticated
        checkUserAuthentication();
    }

    // Check user authentication status
    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If user is not authenticated, navigate to LoginActivity
            startActivity(new Intent(GroupTherapyActivity.this, LoginActivity.class));
            finish(); // Close current activity
        } else {
            // If user is authenticated, initialize rooms
            initializeRooms();
        }
    }

    // Initialize and display rooms
    private void initializeRooms() {
        roomList = new ArrayList<>();
        roomList.add(new Room(1, 6));
        roomList.add(new Room(2, 6));
        roomList.add(new Room(3, 6));

        // Sort rooms by occupancy
        insertionSort(roomList);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Set up RoomAdapter
        roomAdapter = new RoomAdapter(roomList, this);
        recyclerView.setAdapter(roomAdapter);
    }

    // Insertion sort algorithm to sort rooms by occupancy
    public static void insertionSort(ArrayList<Room> rooms) {
        int n = rooms.size();
        for (int i = 1; i < n; ++i) {
            Room key = rooms.get(i);
            int j = i - 1;

            while (j >= 0 && rooms.get(j).getCurrentOccupancy() < key.getCurrentOccupancy()) {
                rooms.set(j + 1, rooms.get(j));
                j = j - 1;
            }
            rooms.set(j + 1, key);
        }
    }

    // Handle room click events
    @Override
    public void onRoomClick(int roomId) {
        // Navigate to the corresponding activity based on room ID
        switch (roomId) {
            case 1:
                startActivity(new Intent(GroupTherapyActivity.this, Room1Activity.class));
                break;
            case 2:
                startActivity(new Intent(GroupTherapyActivity.this, Room2Activity.class));
                break;
            case 3:
                startActivity(new Intent(GroupTherapyActivity.this, Room3Activity.class));
                break;
            default:
                // Show error message if room ID is not found
                Toast.makeText(this, "Belirtilen oda bulunamadı.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
