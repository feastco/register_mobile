package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    String nama,email;
    TextView tvwelcome;
    public static final String URL = new ServerAPI().BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tvwelcome = findViewById(R.id.tvWelcome);

        // Ambil data dari intent
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");

        // Tampilkan nama di TextView
        if (nama != null) {
            tvwelcome.setText("Selamat Datang: " + nama + " (" + email + ")");
        } else {
            tvwelcome.setText("Selamat Datang: Guest");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set grid 2 kolom

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI apiService = retrofit.create(RegisterAPI.class);
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body(); // Ambil data dari response
                    ProductAdapter adapter = new ProductAdapter(ProductActivity.this, productList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Kembali ke Home dan kirim kembali nama pengguna
        super.onBackPressed();
        Intent intent = new Intent(ProductActivity.this, Home.class);
        intent.putExtra("nama", getIntent().getStringExtra("nama")); // Kirim kembali nama pengguna
        intent.putExtra("email", getIntent().getStringExtra("email"));
        startActivity(intent);
        finish(); // Tutup ProductActivity
    }
}