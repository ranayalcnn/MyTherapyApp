package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.List;

public class Room3Activity extends AppCompatActivity {
    private int roomId;
    private List<User> userList;
    private TextView occupancyTextView;
    private FirebaseFirestore firestore;
    private ListenerRegistration userListener;
    private FirebaseAuth mAuth;
    private VoiceCallManager voiceCallManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initializeComponents();
        setupVoiceCallManager();
        listenForUsers();
    }

    /**
     * Initialize UI components and Firestore instance.
     */
    private void initializeComponents() {
        firestore = FirebaseFirestore.getInstance();
        roomId = getIntent().getIntExtra("roomId", -1);

        TextView roomTextView = findViewById(R.id.roomTextView);
        roomTextView.setText("Oda " + roomId);

        occupancyTextView = findViewById(R.id.occupancyTextView);
        userList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        Button voiceSearchButton = findViewById(R.id.voiceSearchButton);
        voiceSearchButton.setOnClickListener(this::startVoiceSearch);
    }

    /**
     * Set up the VoiceCallManager and its listener.
     */
    private void setupVoiceCallManager() {
        voiceCallManager = new VoiceCallManager(this, this::handleVoiceSearchResult);
    }

    private void handleVoiceSearchResult(String result) {
        showToast("Voice search result: " + result);
    }

    /**
     * Start voice search when button is clicked.
     */
    private void startVoiceSearch(View v) {
        voiceCallManager.startVoiceSearch();
    }

    /**
     * Listen for user changes in Firestore.
     */
    private void listenForUsers() {
        userListener = firestore.collection("rooms").document(String.valueOf(roomId)).collection("users")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        showToast("Firestore error: " + error.getMessage());
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            handleDocumentChange(dc);
                        }
                    }
                });
    }

    /**
     * Handle changes in Firestore document.
     */
    private void handleDocumentChange(DocumentChange dc) {
        switch (dc.getType()) {
            case ADDED:
                User user = dc.getDocument().toObject(User.class);
                userEnteredRoom(user);
                break;
            case REMOVED:
                User removedUser = dc.getDocument().toObject(User.class);
                userLeftRoom(removedUser);
                break;
            default:
                break;
        }
    }

    /**
     * Add user to the list when they enter the room.
     */
    private void userEnteredRoom(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
            updateOccupancy();
        }
    }

    /**
     * Remove user from the list when they leave the room.
     */
    private void userLeftRoom(User user) {
        userList.remove(user);
        updateOccupancy();
    }

    /**
     * Update the occupancy rate TextView.
     */
    private void updateOccupancy() {
        int occupancy = userList.size();
        occupancyTextView.setText("Doluluk Oranı: " + occupancy);
    }

    /**
     * Show a toast message.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userListener != null) {
            userListener.remove();
        }
    }
}
