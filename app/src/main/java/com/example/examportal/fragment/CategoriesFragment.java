package com.example.examportal.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.examportal.Adapter.CategoriesAdapter;
import com.example.examportal.ApiService.CategoryApiService;
import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentCategoriesBinding;
import com.example.examportal.databinding.FragmentProfileBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {


    private FragmentCategoriesBinding binding;
    public SessionManager sessionManager;
    private CategoryApiService apiService;

    private List<Category> categoryList=new ArrayList<>();


    public CategoriesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCategoriesBinding.inflate(inflater,container,false);

        categoryList.clear();
        loadData();
        return binding.getRoot();


    }

    private void loadData() {


       HandleProgressBar handleProgressBar=new HandleProgressBar(binding.loader);
       handleProgressBar.show();

        sessionManager=new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(CategoryApiService.class);

        String accessToken = sessionManager.getAccessToken();
        if (accessToken!=null)
        {
            Call<List<Category>> call = apiService.getAllCategories("Bearer " + accessToken);

            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                    if (response.isSuccessful() && response.body()!=null)
                    {
                        List<Category> body = response.body();

                        for (Category c:body)
                        {
                            categoryList.add(c);
                            Log.d("Category",c.title);
                        }

                        CategoriesAdapter adapter=new CategoriesAdapter(getContext(),categoryList,getActivity().getSupportFragmentManager(), new QuizFragment(),new AddCategoryFragment());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        handleProgressBar.dismiss();



                    }
                    else {
                        Toast.makeText(getContext(),"failed",Toast.LENGTH_SHORT).show();
                        handleProgressBar.dismiss();
                    }



                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                    Toast.makeText(getContext(),t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    handleProgressBar.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getContext(),"Token is null", Toast.LENGTH_SHORT).show();
            handleProgressBar.dismiss();
        }
    }



}