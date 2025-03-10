package com.example.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvRegister;
    ProgressDialog pd;
    Button btn_login;
    TextView etNama, etPassword;

    public static final String URL = new ServerAPI().BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainRegister.class);
                startActivity(intent);
                finish();
            }
        });

        etNama = findViewById(R.id.etNama);
        etPassword = findViewById(R.id.etPassword);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(view.getContext());
                pd.setTitle("Process Login...");
                pd.setMessage("Tunggu Sebentar...");
                pd.setCancelable(true);
                pd.setIndeterminate(true);
                prosesLogin(etNama.getText().toString(), etPassword.getText().toString());
            }
        });
    }

        void prosesLogin(String vusername, String vpassword) {
            pd.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RegisterAPI api = retrofit.create(RegisterAPI.class);
            api.login(vusername, vpassword).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        // Cek apakah user ditemukan atau tidak
                        if (json.getString("result").equals("1")) {
                            // Cek status aktif atau tidak
                            if (json.getJSONObject("data").getString("status").equals("1")) {
                                Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.putExtra("username", json.getJSONObject("data").getString("username"));
                                startActivity(intent);
                                finish();
                            } else {
                                pd.dismiss();
                                AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                                msg.setMessage("Status User Ini Tidak Aktif")
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } else {
                            pd.dismiss();
                            AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity  .this);
                            msg.setMessage("UserName atau Password Salah")
                                    .setNegativeButton("Retry", null)
                                    .create().show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Info Load", "Load Gagal " + t.toString());
                    pd.dismiss();
                }
            });
    }
}