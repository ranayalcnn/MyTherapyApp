package com.example.therapyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String userId;
    private FirebaseFirestore db;
    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();
        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        listViewMessages.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Kullanıcı oturumu kontrolü
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = currentUser.getUid();
        loadMessages();
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gönderen kişiyi tanımlama
        Message message = new Message(userId, messageText, System.currentTimeMillis());

        // Mesajı Firestore'a ekleme
        db.collection("userMessages").document(userId).collection("messages").add(message)
                .addOnSuccessListener(documentReference -> {
                    editTextMessage.setText("");
                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());
    }

    private void loadMessages() {
        CollectionReference messagesRef = db.collection("userMessages").document(userId).collection("messages");

        // Kullanıcının kendi mesajlarını alacak şekilde filtreleme yapma
        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                    return;
                }

                messageList.clear();
                for (DocumentSnapshot document : snapshots) {
                    Message message = document.toObject(Message.class);
                    messageList.add(message);
                }

                // Mesajları tersine çevirerek en son gönderilenin en alta gelmesini sağla
                Collections.reverse(messageList);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }
}