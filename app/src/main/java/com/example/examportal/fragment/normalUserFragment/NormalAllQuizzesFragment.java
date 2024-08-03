package com.example.examportal.fragment.normalUserFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.Adapter.NormalAdapter.AllQuizAdapter;
import com.example.examportal.ApiService.QuizApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentNormalAllQuizzesBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Quiz;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NormalAllQuizzesFragment extends Fragment {

    private FragmentNormalAllQuizzesBinding binding;

    private SessionManager sessionManager;

    private QuizApiService quizApiService;

    private List<Quiz> quizList = new ArrayList<>();

    public NormalAllQuizzesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNormalAllQuizzesBinding.inflate(inflater, container, false);

        long cid = getArguments().getLong("cid1");
        quizList.clear();
        if (cid != 0) {
            quizList.clear();
            loadData(cid);
        } else {
            quizList.clear();
            getAllQuiz();
        }


        return binding.getRoot();
    }

    private void loadData(long cid) {

        sessionManager = new SessionManager(getContext());
        quizApiService = RetrofitClient.getClient().create(QuizApiService.class);
        String accessToken = sessionManager.getAccessToken();
        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar5);
        if (accessToken != null) {
            progressBar.show();
            Call<List<Quiz>> quizzesByCategory = quizApiService.getQuizzesByCategory("Bearer " + accessToken, cid);
            quizzesByCategory.enqueue(new Callback<List<Quiz>>() {
                @Override
                public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Quiz> quizzes = response.body();
                        for (Quiz quiz : quizzes) {
                            quizList.add(quiz);
                        }
                        AllQuizAdapter adapter = new AllQuizAdapter(quizList, getContext());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Quiz>> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAllQuiz() {
        sessionManager = new SessionManager(getContext());
        quizApiService = RetrofitClient.getClient().create(QuizApiService.class);
        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar5);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            progressBar.show();
            Call<List<Quiz>> allQuiz = quizApiService.getAllQuiz("Bearer " + accessToken);
            allQuiz.enqueue(new Callback<List<Quiz>>() {
                @Override
                public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        List<Quiz> quizzes = response.body();
                        for (Quiz quiz : quizzes) {
                            quizList.add(quiz);
                        }

                        AllQuizAdapter adapter = new AllQuizAdapter(quizList, getContext());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Quiz>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });
        }

    }
}