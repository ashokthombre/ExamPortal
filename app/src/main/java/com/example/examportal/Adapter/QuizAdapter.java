package com.example.examportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examportal.ApiService.QuizApiService;
import com.example.examportal.R;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.model.exam.Quiz;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {


    Context context;
    List<Quiz> list=new ArrayList<>();
    FragmentManager fm;
    Fragment fragment;
    private SessionManager sessionManager;
    private QuizApiService quizApiService;
    Fragment questionFragment;

    public QuizAdapter(Context context, List<Quiz> list, FragmentManager fm, Fragment fragment,Fragment questionFragment) {
        this.context = context;
        this.list = list;
        this.fm=fm;
        this.fragment=fragment;
        this.questionFragment=questionFragment;
    }

    @NonNull
    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.ViewHolder holder, int position) {

        Quiz quiz=list.get(position);

        holder.title.setText(quiz.getTitle());
        holder.description.setText(quiz.getDescription());
        holder.maxMarks.setText("MaxMarks:"+quiz.getMaxMarks());
        holder.noOFQuestions.setText("Questions:"+quiz.getNumberOfQuestions());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteQuiz(quiz);

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putLong("cid",-2);
                bundle.putSerializable("QUIZ",quiz);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putLong("cid",quiz.getqId());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putLong("qid",quiz.getqId());
                questionFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,questionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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
        TextView maxMarks;
        TextView noOFQuestions;
        Button update;
        Button questions;
        Button attempts;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.qTitle);
            description=itemView.findViewById(R.id.qDescription);
            maxMarks=itemView.findViewById(R.id.maxMarks);
            noOFQuestions=itemView.findViewById(R.id.questions);
            questions=itemView.findViewById(R.id.questionBtn);
            update=itemView.findViewById(R.id.updateBtn);
            attempts=itemView.findViewById(R.id.attemptsBtn);
            delete=itemView.findViewById(R.id.deleteBtn);

        }
    }

    private void deleteQuiz(Quiz quiz) {


        sessionManager=new SessionManager(context);
        quizApiService=RetrofitClient.getClient().create(QuizApiService.class);

        String accessToken = sessionManager.getAccessToken();

        if (accessToken!=null)
        {

            Call<Void> voidCall = quizApiService.deleteQuiz("Bearer " + accessToken, quiz.getqId());

            voidCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                    {
                        list.remove(quiz);
                        Toast.makeText(context, "Quiz Deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "Something went wrong,,", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



}
