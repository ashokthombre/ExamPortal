package com.example.examportal.Adapter.NormalAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examportal.R;
import com.example.examportal.model.exam.Category;

import org.w3c.dom.Text;

import java.util.List;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    FragmentManager fm;
    Fragment fragment;

    public AllCategoriesAdapter(Context context, List<Category> categoryList, FragmentManager fm, Fragment fragment) {
        this.context = context;
        this.categoryList = categoryList;
        this.fm = fm;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AllCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_all_categories, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoriesAdapter.ViewHolder holder, int position) {

        Category category = categoryList.get(position);
        holder.title.setText(category.getTitle());
        holder.description.setText(category.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putLong("cid1", category.getCid());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.normal_title);
            description = itemView.findViewById(R.id.normal_description);

        }
    }
}
