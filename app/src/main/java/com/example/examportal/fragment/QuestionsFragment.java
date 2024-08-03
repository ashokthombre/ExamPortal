package com.example.examportal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.examportal.Adapter.QuestionAdapter;
import com.example.examportal.ApiService.QuestionApiService;
import com.example.examportal.R;
import com.example.examportal.databinding.FragmentQuestionsBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Question;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionsFragment extends Fragment {

    private FragmentQuestionsBinding binding;

    private SessionManager sessionManager;

    private QuestionApiService questionApiService;

    private List<Question>questionList=new ArrayList<>();



    public QuestionsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentQuestionsBinding.inflate(inflater,container,false);
        long qid = getArguments().getLong("qid");
        if (qid!=0)
        {
            questionList.clear();
            loadData(qid);
        }
        else {
            Log.d("AAAAAAA","AAAAAAAAAA");
        }

        return binding.getRoot();
    }

    private void loadData(long qid) {
    sessionManager =new SessionManager(getContext());
    questionApiService= RetrofitClient.getClient().create(QuestionApiService.class);
        HandleProgressBar progressBar=new HandleProgressBar(binding.progressBar7);
        progressBar.show();;

        String accessToken = sessionManager.getAccessToken();
        if (accessToken!=null)
        {
            Call<List<Question>> allQuestion = questionApiService.getAllQuestion("Bearer "+accessToken, qid);

            allQuestion.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if (response.isSuccessful() && response.body()!=null)
                    {
                        List<Question> body = response.body();
                        for (Question question:body)
                        {
                            questionList.add(question);
                            Log.d("question",question.toString());
                        }
                        QuestionAdapter adapter=new QuestionAdapter(getContext(),questionList,new AddQuizFragment(),getActivity().getSupportFragmentManager());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                       progressBar.dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                     progressBar.dismiss();
                }
            });
        }

    }
}