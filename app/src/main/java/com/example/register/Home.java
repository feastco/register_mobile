package com.example.register;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class Home extends AppCompatActivity {

//    ActivityMainBinding binding;
    @SuppressLint("NonConstantResourceId")
    String username, nama, foto, password, status, email;
    TextView tvwelcome;
    Button btnkeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvwelcome = findViewById(R.id.tvWelcome);
        btnkeluar = findViewById(R.id.btnLogout);
        String username = getIntent().getStringExtra("username").toString();
//        String email = getIntent().getStringExtra("email").toString();

        // Tampilkan username di TextView
        if (username != null) {
            tvwelcome.setText("Selamat Datang: " + username);
        } else {
            tvwelcome.setText("Selamat Datang: Pengguna");
        }

        btnkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish(); // Tutup activity saat ini
            }
        });
    }
}