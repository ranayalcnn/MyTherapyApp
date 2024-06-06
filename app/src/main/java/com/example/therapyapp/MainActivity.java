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

    private static final int SPLASH_DELAY = 3000; // SplashScreen'un görüntülenme süresi (milisaniye cinsinden)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SplashScreen'un görüntülenmesi için bir Handler kullanarak gecikme sağlayın
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // SplashScreen gösterim süresi sona erdiğinde LoginActivity'ye geçiş yap
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // MainActivity'yi geri tuşuyla dönüldüğünde tekrar başlatmasını önler
            }
        }, SPLASH_DELAY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Firebase kullanıcı oturumu kontrolü
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Oturum açmamış kullanıcı varsa LoginActivity'e geçiş yap
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // MainActivity'yi geri tuşuyla dönüldüğünde tekrar başlatmasını önler
            }
    }
}