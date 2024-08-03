package com.example.examportal.Adapter.NormalAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examportal.R;
import com.example.examportal.model.exam.Question;

import java.util.ArrayList;
import java.util.List;

public class AttemptQuizAdapter extends RecyclerView.Adapter<AttemptQuizAdapter.ViewHolder> {

    Context context;
    List<Question>questionList;

    private OnItemClickListener mListener;

    public AttemptQuizAdapter(Context context, List<Question> questionList,OnItemClickListener listener) {
        this.context = context;
        this.questionList = questionList;
        this.mListener=listener;


    }

    public interface OnItemClickListener {
        void onItemClick(String item,int position); // Modify the parameter type as per your data
    }



    @NonNull
    @Override
    public AttemptQuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.attempt_quiz_sample, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptQuizAdapter.ViewHolder holder, int position) {
        Question question=questionList.get(position);

        int displayNumber = position + 1;
        holder.bind(displayNumber);
        holder.questionContent.setText(question.getContent());
        holder.option1.setText(question.getOption1());
        holder.option2.setText(question.getOption2());
        holder.option3.setText(question.getOption3());
        holder.option4.setText(question.getOption4());


        holder.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

               holder.genderradioButton = (RadioButton)holder.itemView.findViewById(checkedRadioButtonId);
                if (checkedRadioButtonId==-1)
                {
                    Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                    mListener.onItemClick("no",position);
                }
                else
                {

                    if (mListener != null) {
                        mListener.onItemClick(holder.genderradioButton.getText().toString().trim(),position);
                    }
                    else {
                        mListener.onItemClick("no",position);

                    }
                   // Toast.makeText(context, holder.genderradioButton.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onItemClick(questionList.get(position).getAnswer());
//                }
//            }
//        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int marks=0;
//                for (int i=0;i<ans.length;i++)
//                {
//                    Log.d("Ans",ans[i]);
//
//                }
//
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionContent,questionNumber;
        RadioButton option1,option2,option3,option4;
        RadioGroup group;
        RadioButton  genderradioButton;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            questionContent=itemView.findViewById(R.id.Aquestion);
            option1=itemView.findViewById(R.id.Aoption1);
            option2=itemView.findViewById(R.id.Aoption2);
            option3=itemView.findViewById(R.id.Aoption3);
            option4=itemView.findViewById(R.id.Aoption4);
            questionNumber=itemView.findViewById(R.id.no);
            group=itemView.findViewById(R.id.group);


        }

        public void bind(int number) {
            questionNumber.setText(String.valueOf("Q."+number+")"));
        }
    }


}
