package com.example.examportal.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examportal.ApiService.CategoryApiService;
import com.example.examportal.R;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context context;
    List<Category> list = new ArrayList<>();
    FragmentManager fm;
    Fragment fragment;
    Fragment addCategory;

    public CategoriesAdapter(Context context, List<Category> list, FragmentManager fm, Fragment fragment,Fragment addCategory) {
        this.context = context;
        this.list = list;
        this.fm = fm;
        this.fragment = fragment;
        this.addCategory=addCategory;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {

        Category category = list.get(position);
        holder.title.setText(category.title);
        holder.description.setText(category.description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putLong("cid",category.getCid());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putParcelable("category",category);
                addCategory.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,addCategory);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                deleteCategory(category);

            }
        });


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        ImageView edit;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cTitle);
            description = itemView.findViewById(R.id.cDescription);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);



        }
    }
    private void deleteCategory(Category category) {

        SessionManager sessionManager=new SessionManager(context);
        CategoryApiService categoryApiService = RetrofitClient.getClient().create(CategoryApiService.class);
        String accessToken = sessionManager.getAccessToken();

        if (accessToken!=null)
        {
            Call<Void> voidCall = categoryApiService.deleteCategory("Bearer " + accessToken, category.getCid());
            voidCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                    {
                        list.remove(category);
                        Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else {
            Toast.makeText(context, "Token Is empty..", Toast.LENGTH_SHORT).show();
        }
    }
}
