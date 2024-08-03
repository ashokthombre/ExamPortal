package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.databinding.ActivityRegisterBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private UserApiService userApiService;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        userApiService= RetrofitClient.getClient().create(UserApiService.class);
        sessionManager=new SessionManager(this);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this,"Clicked",Toast.LENGTH_SHORT).show();

                String firstName = binding.firstNameR.getText().toString().trim();
                String lastName = binding.lastNameR.getText().toString().trim();
                String userName = binding.userNameR.getText().toString().trim();
                String password = binding.passwordR.getText().toString().trim();
                String phone = binding.phone.getText().toString().trim();
                String email = binding.email.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty()|| userName.isEmpty()|| password.isEmpty()||phone.isEmpty()||email.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"Fill all fields..",Toast.LENGTH_SHORT).show();
                }
                else {
                    User user=new User();
                    user.setUsername(userName);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setEmail(email);

                    HandleProgressBar progressBar=new HandleProgressBar(binding.progressBar2);
                    progressBar.show();

                    String accessToken = sessionManager.getAccessToken();

                        Call<User> userCall = userApiService.addUser(user);

                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful() && response.body()!=null)
                                {
                                    User data = response.body();
                                    Log.d("User",data.toString());
                                 //   Toast.makeText(RegisterActivity.this,"User Register Successfully !",Toast.LENGTH_SHORT).show();
                                    progressBar.dismiss();
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"User Register Failed.. !",Toast.LENGTH_SHORT).show();
                                    progressBar.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                                Toast.makeText(RegisterActivity.this,"Error " +t.getMessage(),Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }
                        });


                }




            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}