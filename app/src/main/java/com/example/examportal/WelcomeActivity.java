package com.example.examportal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.examportal.databinding.ActivityWelcomeBinding;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWelcomeBinding.inflate(getLayoutInflater());

        sessionManager = new SessionManager(this);

        String accessToken = sessionManager.getAccessToken();

        String user = sessionManager.getUser();
        Gson gson = new Gson();
        User user1 = gson.fromJson(user, User.class);

        if (user1!=null || accessToken!=null)
        {
            String authority = user1.getAuthorities().get(0).authority;
            Intent intent;
            if (authority.equals("NORMAL"))
            {
                intent = new Intent(WelcomeActivity.this, NormalUserActivity.class);
            }
            else {
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }

//        if (accessToken !=null)
//        {
//            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}