package com.example.examportal.helper;


import android.content.Context;
import android.util.Log;


import com.example.examportal.ApiService.UserApiService;

import com.example.examportal.model.User;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchData {
    public SessionManager sessionManager;
    private UserApiService apiService;
    private User data;


    public FetchData(Context context) {
        apiService = RetrofitClient.getClient().create(UserApiService.class);
        sessionManager = new SessionManager(context);

    }


    public User fetchUserData() {

        Log.d("Fetch", "Fetching Data");

        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {
            Call<User> call = apiService.currentUser("Bearer " + accessToken);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        data = response.body();

                        // Process data as needed
                        Gson gson = new Gson();
                        String json = gson.toJson(data);
                        String user = sessionManager.getUser();
                        if (user == null) {
                            sessionManager.saveUser(json);
                        }
                        Log.d("UserAAAAAAA", data.toString());


                    } else {
//                        Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
//                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle case where token is null (user not logged in or token expired)
        }

        return data;
    }


    public void updateUser(User user) {
        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {
            Call<User> call = apiService.updateUser("Bearer " + accessToken, user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {


                    if (response.isSuccessful() && response.body() != null) {
                        User body = response.body();
                        Log.d("Updated", body.toString());
                        if (body != null) {
                            String user1 = sessionManager.getUser();
                            if (user1 == null) {
                                Gson gson = new Gson();
                                String json = gson.toJson(data);
                                sessionManager.deleteUser();
                                sessionManager.saveUser(json);
                                Log.d("UpdatedUser", body.toString());
                            }

                        }
                    } else {
                        Log.d("Error", "Something went wrong");
                    }


                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                    Log.e("Error", t.getMessage().toString());
                }
            });
        } else {
            Log.d("Error", "Token is empty");
        }

    }
}
