package com.example.examportal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.Adapter.QuizAdapter;
import com.example.examportal.ApiService.QuizApiService;
import com.example.examportal.databinding.FragmentQuizBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Quiz;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;

    public SessionManager sessionManager;
    private QuizApiService apiService;
    private List<Quiz> quizList = new ArrayList<>();

    public QuizFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        QuizAdapter adapter = new QuizAdapter(getContext(), quizList, getActivity().getSupportFragmentManager(), new AddQuizFragment(),new QuestionsFragment());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        long cid = getArguments().getLong("cid");

        if (cid == 0) {
            quizList.clear();
            loadData();
        } else {
            Log.d("Cid", String.valueOf(cid));
            quizList.clear();
            getQuizzes(cid);
        }


        return binding.getRoot();
    }

    private void loadData() {

        HandleProgressBar progressBar = new HandleProgressBar(binding.loader);

        progressBar.show();

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(QuizApiService.class);


        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {

            Call<List<Quiz>> allQuiz = apiService.getAllQuiz("Bearer " + accessToken);

            allQuiz.enqueue(new Callback<List<Quiz>>() {
                @Override
                public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Quiz> quizzes = response.body();
                        for (Quiz quiz : quizzes) {
                            quizList.add(quiz);
                        }
                        QuizAdapter adapter = new QuizAdapter(getContext(), quizList, getActivity().getSupportFragmentManager(), new AddQuizFragment(),new QuestionsFragment());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();

                    } else {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Quiz>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });


        } else {
            Toast.makeText(getContext(), "Token is empty...", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }


    }

    private void getQuizzes(long cid) {

        HandleProgressBar progressBar = new HandleProgressBar(binding.loader);

        progressBar.show();

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(QuizApiService.class);

        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {
            Call<List<Quiz>> quizzesByCategory = apiService.getQuizzesByCategory("Bearer " + accessToken, cid);
            quizzesByCategory.enqueue(new Callback<List<Quiz>>() {
                @Override
                public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Quiz> body = response.body();
                        Log.d("List", body.toString());
                        quizList.clear();
                        for (Quiz quiz : body) {
                            quizList.add(quiz);
                            Log.d("Quiz", quiz.toString());

                        }
                        QuizAdapter adapter = new QuizAdapter(getContext(), quizList, getActivity().getSupportFragmentManager(), new AddQuizFragment(),new QuestionsFragment());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                        progressBar.dismiss();


                    } else {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                        Log.d("Error", "Something went wrong");
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Quiz>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage());
                    progressBar.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Token is null", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }

    }
}