package com.example.therapyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceCallManager {

    private static final int REQUEST_CODE_VOICE_SEARCH = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 2;

    private Context context;
    private VoiceCallListener voiceCallListener;

    public VoiceCallManager(Context context, VoiceCallListener voiceCallListener) {
        this.context = context;
        this.voiceCallListener = voiceCallListener;
    }

    public void startVoiceSearch() {
        if (checkPermissions()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Lütfen konuşun");

            try {
                ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE_VOICE_SEARCH);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Sesli arama başlatılırken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, Manifest.permission.RECORD_AUDIO)) {
                // İzin daha önce reddedildi, kullanıcıya neden bu iznin gerektiği açıklanıyor
                Toast.makeText(context, "Mikrofon erişimi gerekiyor", Toast.LENGTH_SHORT).show();
            }
            // İzin isteniyor
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceSearch();
            } else {
                // İzin reddedildi
                Toast.makeText(context, "Mikrofon erişimi reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VOICE_SEARCH && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                // Tüm sonuçlar kullanıcıya gösteriliyor
                StringBuilder allResults = new StringBuilder();
                for (String res : result) {
                    allResults.append(res).append("\n");
                }
                Toast.makeText(context, "Sesli arama sonuçları:\n" + allResults.toString(), Toast.LENGTH_LONG).show();
                voiceCallListener.onVoiceSearchResult(allResults.toString());
            }
        }
    }

    public interface VoiceCallListener {
        void onVoiceSearchResult(String result);
    }
}