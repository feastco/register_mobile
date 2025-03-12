package com.example.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    String nama,email;
    TextView tvwelcome;
    LinearLayout btnkeluar, btnproduct, btnedit;

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
        btnkeluar = findViewById(R.id.layoutLogout);
        btnproduct = findViewById(R.id.layoutProduct);
        btnedit = findViewById(R.id.layoutEditProfile);

        // Ambil data dari intent
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");

        // Tampilkan nama di TextView
        if (nama != null) {
            tvwelcome.setText("Selamat Datang: " + nama + " (" + email + ")");
        } else {
            tvwelcome.setText("Selamat Datang: Guest");
        }

        // Atur onClickListener untuk btnkeluar
        if (btnkeluar != null) {
            btnkeluar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Kembali ke MainActivity
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Tutup activity saat ini
                }
            });
        } else {
            Log.e("HomeActivity", "btnkeluar is null. Check your layout XML.");
        }

        // Atur onClickListener untuk btnproduct
        if (btnproduct != null) {
            btnproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Buka ProductActivity dan kirim nama pengguna
                    Intent intent = new Intent(Home.this, ProductActivity.class);
                    intent.putExtra("nama", nama); // Kirim nama pengguna ke ProductActivity
                    intent.putExtra("email", email); // Kirim email pengguna ke ProductActivity
                    startActivity(intent);
                    finish(); // Tutup activity saat ini
                }
            });
        } else {
            Log.e("HomeActivity", "btnproduct is null. Check your layout XML.");
        }

        // Atur onClickListener untuk btnproduct
        if (btnedit != null) {
            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Buka ProductActivity dan kirim nama pengguna
                    Intent intent = new Intent(Home.this, MainEditProfile.class);
                    intent.putExtra("nama", nama); // Kirim nama pengguna ke ProductActivity
                    intent.putExtra("email", email); // Kirim email pengguna ke ProductActivity
                    startActivity(intent);
                    finish(); // Tutup activity saat ini
                }
            });
        } else {
            Log.e("HomeActivity", "btnedit is null. Check your layout XML.");
        }
    }
}