package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GroupChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Initialize buttons
        Button groupButton = findViewById(R.id.groupButton);
        Button chatButton = findViewById(R.id.chatButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Set click listener for groupButton
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to GroupTherapyActivity
                startActivity(new Intent(GroupChatActivity.this, GroupTherapyActivity.class));
            }
        });

        // Set click listener for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout user and navigate to LoginActivity
                // Example: SessionManager.logout();
                Intent intent = new Intent(GroupChatActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close current activity to prevent return on back press
            }
        });

        // Set click listener for chatButton
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FilterUsersActivity
                startActivity(new Intent(GroupChatActivity.this, FilterUsersActivity.class));
            }
        });
    }
}
