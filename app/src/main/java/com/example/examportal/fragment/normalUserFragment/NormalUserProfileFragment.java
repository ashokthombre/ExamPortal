package com.example.examportal.fragment.normalUserFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentNormalUserProfileBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NormalUserProfileFragment extends Fragment {

    private FragmentNormalUserProfileBinding binding;
    private SessionManager sessionManager;
    private UserApiService userApiService;
    private Context context;

    public NormalUserProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNormalUserProfileBinding.inflate(inflater, container, false);
        context=getContext();

        SessionManager sessionManager = new SessionManager(getContext());

        String user = sessionManager.getUser();
        Gson gson = new Gson();
        User user1 = gson.fromJson(user, User.class);
        if (user1 != null) {
            binding.NormalUserName.setText(user1.getUsername());
            binding.NormalFirstName.setText(user1.getFirstName());
            binding.NormalLastName.setText(user1.lastName);
            binding.NormalName.setText(user1.getUsername());
            binding.NormalPhone.setText(user1.getPhone());
            binding.NormalEmail.setText(user1.getEmail());
        }


        binding.NormalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = binding.NormalFirstName.getText().toString().trim();
                String lastName = binding.NormalLastName.getText().toString().trim();
                String username = binding.NormalName.getText().toString().trim();
                String phone = binding.NormalPhone.getText().toString().trim();
                String email = binding.NormalEmail.getText().toString().trim();
                String password = binding.NormalPassword.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Fill all fields..", Toast.LENGTH_SHORT).show();
                } else {

                    User user2 = new User();
                    user2.setFirstName(firstName);
                    user2.setLastName(lastName);
                    user2.setUsername(username);
                    user2.setPhone(phone);
                    user2.setEmail(email);
                    user2.setPassword(password);
                    user2.setProfile("default");
                    user2.setId(user1.getId());

                    updateUser(user2);

                    FragmentManager fm = getParentFragmentManager();
                    fm.popBackStack();


                }


            }
        });

        return binding.getRoot();
    }

    private void updateUser(User user2) {

        sessionManager = new SessionManager(getContext());
        userApiService = RetrofitClient.getClient().create(UserApiService.class);
        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar10);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            progressBar.show();
            Call<User> userCall = userApiService.updateUser("Bearer " + accessToken, user2);

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                      //  Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    } else {
                     //   Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                 //   Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });
        } else {
            //Toast.makeText(context, "Token is empty...", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }


    }
}