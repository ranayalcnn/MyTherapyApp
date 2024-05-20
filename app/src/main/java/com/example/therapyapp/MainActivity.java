package com.example.therapyapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Kullanıcıları görüntülemek için bir düğme ekleyin ve bu düğmeye tıklama dinleyicisi ekleyin
        //findViewById(R.id.viewUsersButton).setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
                // Kullanıcıları görüntülemek için UserListActivity'yi başlatın
              //  startActivity(new Intent(MainActivity.this, UserListActivity.class));
           // }
        //});

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}