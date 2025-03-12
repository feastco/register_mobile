package com.example.register;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRegister extends AppCompatActivity {

    public static final String URL = new ServerAPI().BASE_URL;

    TextView tvBack;
    EditText etNama, etEmail, etPassword;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegister.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNama = (EditText) findViewById(R.id.etNama);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUnit(etEmail.getText().toString(),
                        etNama.getText().toString(),
                        etPassword.getText().toString());
            }
        });
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w.-]+@([\\w-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    void processUnit(String vemail, String vname, String vpassword) {
        if (!isEmailValid(vemail)) {
            AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);
            msg.setMessage("Email Tidak Valid")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        api.register(vemail, vname, vpassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d("JSON Response", jsonResponse); // Cetak respons JSON untuk debugging

                        JSONObject json = new JSONObject(jsonResponse);

                        // Periksa apakah kunci "status" ada
                        if (json.has("status")) {
                            String status = json.getString("status");
                            if (status.equals("1")) {
                                if (json.has("result") && json.getString("result").equals("1")) {
                                    AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);
                                    msg.setMessage("Registrasi Berhasil")
                                            .setPositiveButton("OK", null)
                                            .create()
                                            .show();
                                    etEmail.setText("");
                                    etNama.setText("");
                                    etPassword.setText("");
                                } else {
                                    AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);
                                    msg.setMessage("Registrasi Gagal")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } else {
                                AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);
                                msg.setMessage("User sudah ada")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } else {
                            Log.e("JSON Error", "Key 'status' not found in JSON response");
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API Error", "Server returned error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Info Register", "Register Gagal: " + t.toString());
            }
        });
    }
}