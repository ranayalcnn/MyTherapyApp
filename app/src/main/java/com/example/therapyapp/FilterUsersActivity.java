package com.example.therapyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FilterUsersActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView listViewUsers;
    private Spinner spinnerDisorder;
    private Button buttonFilter;
    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_users);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        listViewUsers = findViewById(R.id.listViewUsers);
        spinnerDisorder = findViewById(R.id.spinnerDisorder);
        buttonFilter = findViewById(R.id.buttonFilter);

        // Setup spinner with disorder options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.disorders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisorder.setAdapter(adapter);

        // Setup user list and adapter
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        listViewUsers.setAdapter(userAdapter);

        // Set filter button click listener
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDisorder = spinnerDisorder.getSelectedItem().toString();
                filterUsersByDisorder(selectedDisorder);
            }
        });

        // Set list item click listener
        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);
            // Start ChatActivity and pass user details
            Intent intent = new Intent(FilterUsersActivity.this, ChatActivity.class);
            intent.putExtra("userName", selectedUser.getName());
            startActivity(intent);
        });

        // Load all users initially
        loadUsers();
    }

    // Load all users from Firestore
    private void loadUsers() {
        CollectionReference usersRef = db.collection("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                Log.d("UserMatchActivity", "Users loaded: " + userList.size());
            } else {
                Log.d("UserMatchActivity", "Error getting documents: ", task.getException());
            }
        });
    }

    // Filter users by selected disorder
    private void filterUsersByDisorder(String disorder) {
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("disorder", disorder).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                Log.d("UserMatchActivity", "Filtered users: " + userList.size());
            } else {
                Log.d("UserMatchActivity", "Error filtering documents: ", task.getException());
            }
        });
    }
}
