package com.example.examportal.Adapter.NormalAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examportal.AttemptQuizActivity;
import com.example.examportal.R;
import com.example.examportal.model.exam.Quiz;

import java.util.List;

public class AllQuizAdapter extends RecyclerView.Adapter<AllQuizAdapter.ViewHolder> {

    private List<Quiz>quizList;
    private Context context;

    public AllQuizAdapter(List<Quiz> quizList, Context context) {
        this.quizList = quizList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllQuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_all_quizzes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllQuizAdapter.ViewHolder holder, int position) {

        Quiz quiz=quizList.get(position);
        holder.title.setText(quiz.getTitle());
        holder.description.setText(quiz.getDescription());
        holder.maxMarks.setText("MaxMarks: "+quiz.getMaxMarks());
        holder.noOfQuestion.setText("Questions: "+quiz.getNumberOfQuestions());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, AttemptQuizActivity.class);
                i.putExtra("quiz",quiz.getqId());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,maxMarks,noOfQuestion;
        TextView description;
        Button viewBtn,startBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.nTitle);
            description=itemView.findViewById(R.id.nDescription);
            viewBtn=itemView.findViewById(R.id.viewButton);
            startBtn=itemView.findViewById(R.id.startButton);
            maxMarks=itemView.findViewById(R.id.nMaxMarks);
            noOfQuestion=itemView.findViewById(R.id.nQuestions);
        }
    }
}
