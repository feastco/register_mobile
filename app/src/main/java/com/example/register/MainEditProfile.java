package com.example.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEditProfile extends AppCompatActivity {

    TextView tvWelcome, tvBack;
    EditText etNama, etAlamat, etKota, etProvinsi, etTelp, etKodePos;
    String email;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inisialisasi view
        etNama = (EditText) findViewById(R.id.etProfile_Nama);
        etAlamat = (EditText) findViewById(R.id.etProfile_alamat);
        etKota = (EditText) findViewById(R.id.etProfile_kota);
        etProvinsi = (EditText) findViewById(R.id.etProfile_province);
        etTelp = (EditText) findViewById(R.id.etProfile_telp);
        etKodePos = (EditText) findViewById(R.id.etProfile_kodepos);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome : " + getIntent().getStringExtra("nama") +
                " (" + getIntent().getStringExtra("email") + ")");

        tvBack = findViewById(R.id.tvProfile_Back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        email = getIntent().getStringExtra("email");
        getProfile(email);

        btnSubmit = findViewById(R.id.imgbtnProfile_Submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUser data = new DataUser();
                data.setAlamat(etAlamat.getText().toString());
                data.setNama(etNama.getText().toString());
                data.setKota(etKota.getText().toString());
                data.setProvinsi(etProvinsi.getText().toString());
                data.setTelp(etTelp.getText().toString());
                data.setKodepos(etKodePos.getText().toString());
                data.setEmail(email);

                updateProfile(data);
            }
        });
    }

    void getProfile(String vemail) {
        ServerAPI urlAPI = new ServerAPI();
        String URL = urlAPI.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        api.getProfile(vemail).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("result").equals("1")) {
                        etNama.setText(json.getJSONObject("data").getString("nama"));
                        etAlamat.setText(json.getJSONObject("data").getString("alamat"));
                        etKota.setText(json.getJSONObject("data").getString("kota"));
                        etProvinsi.setText(json.getJSONObject("data").getString("provinsi"));
                        etTelp.setText(json.getJSONObject("data").getString("telp"));
                        etKodePos.setText(json.getJSONObject("data").getString("kodepos"));

                        Log.i("Info Profile", json.getJSONObject("data").getString("nama"));
                    } else {
                        // Handle jika result tidak sama dengan 1
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void updateProfile(DataUser data) {
        ServerAPI urlAPI = new ServerAPI();
        String URL = urlAPI.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        // Log data yang akan dikirim
        Log.d("Update Data", "Nama: " + data.getNama());
        Log.d("Update Data", "Alamat: " + data.getAlamat());
        Log.d("Update Data", "Kota: " + data.getKota());
        Log.d("Update Data", "Provinsi: " + data.getProvinsi());
        Log.d("Update Data", "Telp: " + data.getTelp());
        Log.d("Update Data", "Kode Pos: " + data.getKodepos());
        Log.d("Update Data", "Email: " + data.getEmail());

        Call<ResponseBody> call = api.updateProfile(
                data.getNama(),
                data.getAlamat(),
                data.getKota(),
                data.getProvinsi(),
                data.getTelp(),
                data.getKodepos(),
                data.getEmail()
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        Log.d("Server Response", jsonResponse); // Cetak respons JSON
                        JSONObject json = new JSONObject(jsonResponse);
                        Toast.makeText(MainEditProfile.this, json.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        getProfile(data.getEmail()); // Ambil data terbaru setelah update
                    } else {
                        Log.e("API Error", "Server returned error: " + response.code());
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API Failure", "Gagal memperbarui profil: " + t.getMessage());
                AlertDialog.Builder msg = new AlertDialog.Builder(MainEditProfile.this);
                msg.setMessage("Gagal memperbarui profil: " + t.getMessage())
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Kembali ke Home dan kirim kembali nama pengguna
        super.onBackPressed();
        Intent intent = new Intent(MainEditProfile.this, Home.class);
        intent.putExtra("nama", getIntent().getStringExtra("nama")); // Kirim kembali nama pengguna
        intent.putExtra("email", getIntent().getStringExtra("email"));
        startActivity(intent);
        finish(); // Tutup ProductActivity
    }
}