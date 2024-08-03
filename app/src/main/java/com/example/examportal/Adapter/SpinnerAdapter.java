package com.example.examportal.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.examportal.R;
import com.example.examportal.model.exam.Category;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter  {

    private ArrayList<Category> categoryList=new ArrayList<>();
    private Context context;

    public SpinnerAdapter(ArrayList<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view==null)
        {
          view= LayoutInflater.from(context).inflate(R.layout.sample_spinner, viewGroup, false);

          TextView textView=view.findViewById(R.id.cName);
          Category category=categoryList.get(i);
          textView.setText(category.getTitle());
        }
        return view;
    }



}
