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

        mAuth = FirebaseAuth.getInstance();

        // Kullanıcı oturum açmış mı diye kontrol et
        checkUserAuthentication();
    }

    private void checkUserAuthentication() {
        // Firebase Authentication örneği al
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Kullanıcı oturum açmış mı kontrol et
        if (currentUser == null) {
            // Kullanıcı oturum açmamışsa, LoginActivity'e yönlendir
            startActivity(new Intent(GroupTherapyActivity.this, LoginActivity.class));
            // Bu aktiviteyi kapat
            finish();
        } else {
            // Kullanıcı oturum açmışsa, odaları listele
            initializeRooms();
        }
    }

    private void initializeRooms() {
        // Odaları oluştur ve listele
        roomList = new ArrayList<>();
        roomList.add(new Room(1, 6));
        roomList.add(new Room(2, 6));
        roomList.add(new Room(3, 6));

        // Odaları doluluk oranına göre sırala
        insertionSort(roomList);

        // RecyclerView'ı bul ve ayarla
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // RoomAdapter'ı oluştur ve RecyclerView'a set et
        roomAdapter = new RoomAdapter(roomList, this); // Odalara tıklama dinleyicisini (listener) ekledik
        recyclerView.setAdapter(roomAdapter);
    }

    // Insertion Sort algoritması
    public static void insertionSort(ArrayList<Room> rooms) {
        int n = rooms.size();
        for (int i = 1; i < n; ++i) {
            Room key = rooms.get(i);
            int j = i - 1;

            // Odaları doluluk oranına göre sırala
            while (j >= 0 && rooms.get(j).getCurrentOccupancy() < key.getCurrentOccupancy()) {
                rooms.set(j + 1, rooms.get(j));
                j = j - 1;
            }
            rooms.set(j + 1, key);
        }
    }

    // Odalara tıklandığında gerçekleşecek eylem
    @Override

    public void onRoomClick(int roomId) {
        // Tıklanan oda numarasına göre ilgili aktiviteye yönlendirme yapılabilir
        // Örneğin, tıklanan odaya göre farklı aktivitelere yönlendirme yapılabilir
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
                // Varsayılan olarak bir işlem yapılabilir veya hata mesajı gösterilebilir
                Toast.makeText(this, "Belirtilen oda bulunamadı.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
