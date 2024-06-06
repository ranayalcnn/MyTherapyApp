package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.List;

public class Room1Activity extends AppCompatActivity {
    private int roomId;
    private List<User> userList; // List of users in the room
    private TextView occupancyTextView; // TextView to display occupancy rate
    private FirebaseFirestore firestore;
    private ListenerRegistration userListener;

    private FirebaseAuth mAuth;
    private VoiceCallManager voiceCallManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get roomId from Intent
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", -1);

        // Set up the page based on roomId
        TextView roomTextView = findViewById(R.id.roomTextView);
        roomTextView.setText("Room " + roomId);

        // Initialize the occupancy TextView
        occupancyTextView = findViewById(R.id.occupancyTextView);

        // Initialize the list of users in the room
        userList = new ArrayList<>();

        // Listen for users in the room
        listenForUsers();

        mAuth = FirebaseAuth.getInstance();

        // Create VoiceCallManager instance
        voiceCallManager = new VoiceCallManager(Room1Activity.this, new VoiceCallManager.VoiceCallListener() {
            @Override
            public void onVoiceSearchResult(String result) {
                // Handle voice search results
                // For example, display the result in a TextView
                // textView.setText(result);
            }
        });

        // Set up the voice search button click listener
        Button voiceSearchButton = findViewById(R.id.voiceSearchButton);
        voiceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start voice search with VoiceCallManager
                voiceCallManager.startVoiceSearch();
            }
        });
    }

    // Listen for users in the room from Firestore
    private void listenForUsers() {
        userListener = firestore.collection("rooms").document(String.valueOf(roomId)).collection("users")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle errors if necessary
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    // When a user is added
                                    User user = dc.getDocument().toObject(User.class);
                                    userEnteredRoom(user);
                                    break;
                                case REMOVED:
                                    // When a user is removed
                                    User removedUser = dc.getDocument().toObject(User.class);
                                    userLeftRoom(removedUser);
                                    break;
                                // Additional cases can be added if needed
                            }
                        }
                    }
                });
    }

    // Called when a user enters the room
    private void userEnteredRoom(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
            // Update occupancy
            updateOccupancy();
        }
    }

    // Called when a user leaves the room
    private void userLeftRoom(User user) {
        if (userList.contains(user)) {
            userList.remove(user);
            // Update occupancy
            updateOccupancy();
        }
    }

    // Update the occupancy rate
    private void updateOccupancy() {
        int occupancy = userList.size(); // Number of users in the room
        occupancyTextView.setText("Occupancy Rate: " + occupancy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener
        if (userListener != null) {
            userListener.remove();
        }
    }
}
