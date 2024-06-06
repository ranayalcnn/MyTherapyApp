package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // Splash screen display duration in milliseconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a Handler to delay the splash screen display
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to LoginActivity after the splash screen duration ends
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Prevent MainActivity from restarting when the back button is pressed
            }
        }, SPLASH_DELAY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check Firebase user authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If the user is not authenticated, navigate to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevent MainActivity from restarting when the back button is pressed
        }
    }
}
