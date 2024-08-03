package com.example.examportal.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.ApiService.CategoryApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentAddCategoryBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryFragment extends Fragment {

    private FragmentAddCategoryBinding binding;
    public SessionManager sessionManager;
    private CategoryApiService apiService;


    public AddCategoryFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);

        Bundle bundle=getArguments();

        if (bundle!=null)
        {
           Category category=bundle.getParcelable("category");
            Log.d("Category",category.toString());

            binding.addTitle.setText(category.getTitle());
            binding.addDecription.setText(category.getDescription());
            binding.updateCategoryBtn.setVisibility(View.VISIBLE);
            binding.addCategoryBtn.setVisibility(View.GONE);

            binding.updateCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title=binding.addTitle.getText().toString().trim();
                    String description=binding.addDecription.getText().toString().trim();
                    if (title.isEmpty() || description.isEmpty())
                    {
                        Toast.makeText(getContext(), "Fill all fields.. ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Category category1=new Category();
                        category1.setDescription(description);
                        category1.setTitle(title);
                        category1.setCid(category.getCid());

                        updateCategory(category1);

                        FragmentManager fm=getParentFragmentManager();
                        fm.popBackStack();
                    }
                }
            });


        }
        else
        {
            Log.d("Nothing","nothing");
        }

        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.addTitle.getText().toString().trim();
                String Description = binding.addDecription.getText().toString().trim();

                if (title.isEmpty() || Description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill all fields..", Toast.LENGTH_SHORT).show();

                } else {
                    Category category = new Category();
                    category.setTitle(title);
                    category.setDescription(Description);
                    addCategory(category);

                    FragmentManager fm=getParentFragmentManager();
                    fm.popBackStack();
                }
            }
        });


        return binding.getRoot();
    }



    private void updateCategory(Category category1) {

        sessionManager=new SessionManager(getContext());
        apiService=RetrofitClient.getClient().create(CategoryApiService.class);
        HandleProgressBar progressBar=new HandleProgressBar(binding.progressBar3);
        progressBar.show();

        String accessToken = sessionManager.getAccessToken();

        if (accessToken!=null)
        {
            Call<Category> updateCategory = apiService.updateCategory("Bearer " + accessToken, category1);

            updateCategory.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {

                    if (response.isSuccessful() && response.body()!=null)
                    {
                        progressBar.dismiss();
                       // Toast.makeText(getContext() ,"Category Updated", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        progressBar.dismiss();
                       // Toast.makeText(getContext(), "something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {

                    progressBar.dismiss();
                   // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        else
        {
            progressBar.dismiss();
           // Toast.makeText(getContext(), "Token is empty", Toast.LENGTH_SHORT).show();
        }


    }

    private void addCategory(Category category) {
          HandleProgressBar progressBar=new HandleProgressBar(binding.progressBar3);
          progressBar.show();

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(CategoryApiService.class);

        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {
            Call<Category> categoryCall = apiService.addCategory("Bearer " + accessToken, category);
            categoryCall.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        progressBar.dismiss();
                        //oast.makeText(requireContext(), "Category Added..", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.dismiss();
                       // Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    progressBar.dismiss();
                   // Toast.makeText(requireContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        else {
            //Toast.makeText(requireContext(),"Token is null", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }


    }
}