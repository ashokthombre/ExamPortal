package com.example.examportal.fragment.normalUserFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.Adapter.NormalAdapter.AllCategoriesAdapter;
import com.example.examportal.ApiService.CategoryApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentNormalUserCategoriesBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NormalUserCategoriesFragment extends Fragment {

    private FragmentNormalUserCategoriesBinding binding;
    private SessionManager sessionManager;
    private CategoryApiService categoryApiService;
    private List<Category> categoryList = new ArrayList<>();


    public NormalUserCategoriesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNormalUserCategoriesBinding.inflate(inflater, container, false);

        categoryList.clear();
        atAllCategories();
        return binding.getRoot();
    }

    private void atAllCategories() {

        sessionManager = new SessionManager(getContext());
        categoryApiService = RetrofitClient.getClient().create(CategoryApiService.class);

        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar6);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            progressBar.show();
            Call<List<Category>> allCategories = categoryApiService.getAllCategories("Bearer " + accessToken);
            allCategories.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                    if (response.isSuccessful() && response != null) {
                        List<Category> categories = response.body();
                        categoryList.clear();
                        for (Category category : categories) {
                            categoryList.add(category);
                        }

                        AllCategoriesAdapter adapter = new AllCategoriesAdapter(getContext(), categoryList, getActivity().getSupportFragmentManager(), new NormalAllQuizzesFragment());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });

        }

    }
}