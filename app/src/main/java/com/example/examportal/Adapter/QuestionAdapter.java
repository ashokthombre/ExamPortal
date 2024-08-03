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

import com.example.examportal.ApiService.QuestionApiService;
import com.example.examportal.R;
import com.example.examportal.model.exam.Question;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context context;
    private List<Question>questionList;
    FragmentManager fm;
    Fragment fragment;


    public QuestionAdapter(Context context, List<Question> questionList, Fragment fragment, FragmentManager supportFragmentManager) {
        this.context = context;
        this.questionList = questionList;
        this.fragment=fragment;
        this.fm=supportFragmentManager;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_question, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {

        Question question=questionList.get(position);
        int displayNumber = position + 1;
          holder.bind(displayNumber);

        holder.questionContent.setText(question.content);
        holder.option1.setText(question.option1);
        holder.option2.setText(question.option2);
        holder.option3.setText(question.option3);
        holder.option4.setText(question.option4);
        holder.correctAnswer.setText(question.answer);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle=new Bundle();
                bundle.putLong("cid",-1);
                bundle.putSerializable("question",question);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteQuestion(question);
            }
        });

    }



    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionContent,option1,option2,option3,option4,correctAnswer,questionNumber;
        ImageView edit,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionContent=itemView.findViewById(R.id.questionContent);
            option1=itemView.findViewById(R.id.Qoption1);
            option2=itemView.findViewById(R.id.Qoption2);
            option3=itemView.findViewById(R.id.Qoption3);
            option4=itemView.findViewById(R.id.Qoption4);
            correctAnswer=itemView.findViewById(R.id.correctAnswer);
            questionNumber=itemView.findViewById(R.id.queationNumber);
            edit=itemView.findViewById(R.id.editQuestion);
            delete=itemView.findViewById(R.id.deletImage);

        }

        public void bind(int number) {
            questionNumber.setText(String.valueOf(number+")"));
        }

    }
    private void deleteQuestion(Question question) {
        SessionManager sessionManager=new SessionManager(context);
        QuestionApiService questionApiService = RetrofitClient.getClient().create(QuestionApiService.class);

        String accessToken = sessionManager.getAccessToken();
        if (accessToken!=null)
        {
            Call<Void> voidCall = questionApiService.deleteQuestion("Bearer " + accessToken, question.getQuesId());

            voidCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                    {
                        questionList.remove(question);
                        Toast.makeText(context, "question deleted..", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show();
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
