package com.example.examportal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.MainActivity;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentProfileBinding;
import com.example.examportal.helper.FetchData;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
//        View view= inflater.inflate(R.layout.fragment_profile, container, false);


        SessionManager sessionManager = new SessionManager(this.getActivity());
        String user = sessionManager.getUser();
        Gson gson = new Gson();
        User user1 = gson.fromJson(user, User.class);

        if (user1 != null) {
            binding.pUserName.setText(user1.username);
            binding.pFirstName.setText(user1.firstName);
            binding.pLastName.setText(user1.lastName);
            binding.puserName.setText(user1.username);
            binding.pPhone.setText(user1.phone);
            binding.pEmail.setText(user1.email);
        }


        binding.pUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = binding.pUserName.getText().toString().trim();
                String firstName = binding.pFirstName.getText().toString().trim();
                String lastName = binding.pLastName.getText().toString().trim();
                String password = binding.pPassword.getText().toString().trim();
                String phone = binding.pPhone.getText().toString().trim();
                String email = binding.pEmail.getText().toString().trim();

                if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Set All Fields", Toast.LENGTH_SHORT).show();
                } else {


                    User user2 = new User();
                    user2.setUsername(userName);
                    user2.setFirstName(firstName);
                    user2.setLastName(lastName);
                    user2.setPassword(password);
                    user2.setPhone(phone);
                    user2.setEmail(email);
                    user2.setProfile("default");
                    user2.setId(user1.getId());


                    FetchData fetchData = new FetchData(getContext());
                    fetchData.updateUser(user2);


                    FragmentManager fm = getParentFragmentManager();
                    fm.popBackStack();

                    Toast.makeText(getContext(), "User Updated", Toast.LENGTH_SHORT).show();


                }


            }
        });


        return binding.getRoot();
    }


}