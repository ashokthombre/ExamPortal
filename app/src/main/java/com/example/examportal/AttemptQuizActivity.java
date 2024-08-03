package com.example.examportal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.examportal.Adapter.NormalAdapter.AttemptQuizAdapter;
import com.example.examportal.ApiService.QuestionApiService;
import com.example.examportal.databinding.ActivityAttemptQuizBinding;
import com.example.examportal.helper.CustomAlertDialog;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Question;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttemptQuizActivity extends AppCompatActivity implements AttemptQuizAdapter.OnItemClickListener {

    private ActivityAttemptQuizBinding binding;
    private SessionManager sessionManager;
    private QuestionApiService questionApiService;
    private List<Question> questionList = new ArrayList<>();
    private long mStartTimeInMillis = 10 * 60 * 1000; // 10 minutes in milliseconds
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private CountDownTimer mCountDownTimer;
    private String ans[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttemptQuizBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        long qId = getIntent().getLongExtra("quiz", 0);
        Log.d("qid", String.valueOf(qId));

        if (qId != 0) {
            questionList.clear();
            loadData(qId);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                CustomAlertDialog alertDialog = new CustomAlertDialog(AttemptQuizActivity.this, "Are you want to Exit Quiz ?", "Yes", "NO");
                alertDialog.show();

            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        binding.submitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit(ans, questionList);

            }
        });


    }

    private void submit(String[] ans, List<Question> questionList) {

        if (ans != null && questionList != null) {

            int mark = 0;
            for (int i = 0; i < ans.length; i++) {
                if (ans[i].equals(questionList.get(i).getAnswer())) {
                    mark++;
                } else {
                    Log.d("Error", "Array or list get null");
                }
            }

            Intent intent = new Intent(AttemptQuizActivity.this, FinishQuizActivity.class);
            intent.putExtra("marks", mark);
            intent.putExtra("total", questionList.size());
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "EMPTY Array ", Toast.LENGTH_SHORT).show();
        }


    }


    private void loadData(long qId) {
        sessionManager = new SessionManager(AttemptQuizActivity.this);
        questionApiService = RetrofitClient.getClient().create(QuestionApiService.class);
        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar8);
        String accessToken = sessionManager.getAccessToken();


        if (accessToken != null) {
            progressBar.show();
            Call<List<Question>> allQuestion = questionApiService.getAllQuestion("Bearer " + accessToken, qId);
            allQuestion.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        List<Question> body = response.body();

                        for (Question question : body) {
                            questionList.add(question);
                        }
                        ans = new String[questionList.size()];
                        startTimer(questionList.size(), questionList);
                        for (int i = 0; i < questionList.size(); i++) {
                            ans[i] = "NO";
                        }
                        AttemptQuizAdapter adapter = new AttemptQuizAdapter(AttemptQuizActivity.this, questionList, AttemptQuizActivity.this);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AttemptQuizActivity.this));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();
                    } else {
                        progressBar.dismiss();
                        Toast.makeText(AttemptQuizActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(AttemptQuizActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            progressBar.dismiss();
            Toast.makeText(this, "Token is empty", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemClick(String item, int position) {

        ans[position] = item;
    }

    private void startTimer(int size, List<Question> questions) {

        if (size != 0) {
            mTimeLeftInMillis = size * 60 * 1000;
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
//                    // Timer finished
//                    mTimeLeftInMillis = 0;
//                    updateCountDownText();
//                    // Perform any actions you need when timer finishes
//                    // For example, finish the activity
//                    finish();

                    submit(ans, questions);
                }
            }.start();

        } else {
            binding.textView22.setVisibility(View.VISIBLE);
            binding.submitQuiz.setClickable(false);
        }


    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        binding.timer.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


}