package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.databinding.ActivityLoginBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.Authority;
import com.example.examportal.model.JwtRequest;
import com.example.examportal.model.JwtToken;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserApiService apiService;
    private SessionManager sessionManager;
    public User saveUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));


        apiService = RetrofitClient.getClient().create(UserApiService.class);
        sessionManager = new SessionManager(this);


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = binding.userName.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                Log.d("userName", userName);
                Log.d("password", password);
                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fill all fields..", Toast.LENGTH_SHORT).show();
                } else {
                    performLogin(userName, password);
                }


            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.newHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin(String userName, String password) {

        HandleProgressBar progressBar = new HandleProgressBar(binding.progress);

        progressBar.show();

        JwtRequest loginRequest = new JwtRequest(userName, password);
        Call<JwtToken> call = apiService.login(loginRequest);
        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtToken loginResponse = response.body();
                    if (loginResponse.getToken() != null) {
                        //save token sharedPrefrence

                        sessionManager.saveAccessToken(loginResponse.getToken());
                        Log.d("Token", loginResponse.getToken());

                        Call<User> userCall = apiService.currentUser("Bearer " + loginResponse.getToken());
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    User body = response.body();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(body);
                                    String user = sessionManager.getUser();
                                    if (user == null) {
                                        sessionManager.saveUser(json);
                                    }

                                    String authority = body.getAuthorities().get(0).getAuthority();
                                    Log.d("AUTHORITY", authority);


                                    if (authority.equals("NORMAL")) {
                                        Intent intent = new Intent(LoginActivity.this, NormalUserActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                        startActivity(intent);
                                        finishAffinity();
                                    }

                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                        // Redirect to next activity or perform desired action
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }


}