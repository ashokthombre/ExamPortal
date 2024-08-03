package com.example.examportal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.examportal.R;
import com.example.examportal.databinding.FragmentAragmentBinding;
import com.example.examportal.databinding.FragmentCategoriesBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.SessionManager;
import com.google.gson.Gson;

public class AFragment extends Fragment {

   private FragmentAragmentBinding binding;


    public AFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // View view =inflater.inflate(R.layout.fragment_aragment, container, false);
       binding= FragmentAragmentBinding.inflate(inflater,container,false);

       // ProgressBar customLoader=view.findViewById(R.id.custom_loader);

        HandleProgressBar handleProgressBar=new HandleProgressBar(binding.loader);

        handleProgressBar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleProgressBar.dismiss();
            }
        },4000);



      //  TextView textView=view.findViewById(R.id.sample_text);

        SessionManager sessionManager=new SessionManager(this.getActivity());
        String user = sessionManager.getUser();
        Gson gson=new Gson();
        User u= gson.fromJson(user, User.class);

        String fullname=u.firstName+" "+u.lastName;
        binding.sampleText.setText(fullname);

        return binding.getRoot();
    }




}