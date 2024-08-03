package com.example.examportal.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.example.examportal.Adapter.SpinnerAdapter;
import com.example.examportal.ApiService.CategoryApiService;
import com.example.examportal.ApiService.QuestionApiService;

import com.example.examportal.ApiService.QuizApiService;
import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.databinding.FragmentAddQuizBinding;
import com.example.examportal.helper.HandleProgressBar;
import com.example.examportal.model.exam.Category;
import com.example.examportal.model.exam.Question;
import com.example.examportal.model.exam.Quiz;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddQuizFragment extends Fragment {
    private FragmentAddQuizBinding binding;
    private Long qId;
    public SessionManager sessionManager;
    private QuestionApiService apiService;
    private CategoryApiService categoryApiService;
    private QuizApiService quizApiService;
    private SpinnerAdapter adapter;
    private ArrayList<Category> categoryList = new ArrayList<>();
    private Context context;

    public AddQuizFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddQuizBinding.inflate(inflater, container, false);
        context=getContext();
        long qid = getArguments().getLong("cid");
        Log.d("CID", String.valueOf(qid));
        if (qid != 0 && qid > 0) {
            qId = qid;
            Log.d("CID", String.valueOf(qid));
            binding.textView5.setVisibility(View.VISIBLE);
            binding.qContent.setVisibility(View.VISIBLE);
            binding.option1.setVisibility(View.VISIBLE);
            binding.option2.setVisibility(View.VISIBLE);
            binding.option3.setVisibility(View.VISIBLE);
            binding.option4.setVisibility(View.VISIBLE);
            binding.answer.setVisibility(View.VISIBLE);
            binding.check.setVisibility(View.VISIBLE);
            binding.publish.setVisibility(View.VISIBLE);
            binding.addQuiestiontn.setVisibility(View.VISIBLE);


            binding.addQuiestiontn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String content = binding.qContent.getText().toString().trim();
                    String option1 = binding.option1.getText().toString().trim();
                    String option2 = binding.option2.getText().toString().trim();
                    String option3 = binding.option3.getText().toString().trim();
                    String option4 = binding.option4.getText().toString().trim();
                    String answer = binding.answer.getText().toString().trim();

                    boolean checked = binding.check.isChecked();

                    if (content.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
                        Toast.makeText(context, "fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        Question question = new Question();
                        question.setContent(content);
                        question.setOption1(option1);
                        question.setOption2(option2);
                        question.setOption3(option3);
                        question.setOption4(option4);
                        question.setAnswer(answer);
                        question.setImage("default");

                        Quiz quiz = new Quiz();
//                      quiz.setActive(checked);
                        quiz.setqId(qId);

                        question.setQuiz(quiz);

                        addQuestion(question);

                        FragmentManager fm = getParentFragmentManager();
                        fm.popBackStack();


                    }


                }
            });

        } else if (qid == 0) {

            categoryList.clear();
            getCategories();

            binding.textView6.setVisibility(View.VISIBLE);
            binding.quizTitle.setVisibility(View.VISIBLE);
            binding.quizDescription.setVisibility(View.VISIBLE);
            binding.quizMaxMarks.setVisibility(View.VISIBLE);
            binding.quizNumberOfQuestion.setVisibility(View.VISIBLE);
            binding.spinner.setVisibility(View.VISIBLE);
            binding.QuizActive.setVisibility(View.VISIBLE);
            binding.addQuizBtn.setVisibility(View.VISIBLE);
            binding.textView7.setVisibility(View.VISIBLE);

            adapter = new SpinnerAdapter(categoryList, requireContext());
            binding.spinner.setAdapter(adapter);


            binding.addQuizBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String quizTitle = binding.quizTitle.getText().toString().trim();
                    String quizDescription = binding.quizDescription.getText().toString().trim();
                    String quizMaxMarks = binding.quizMaxMarks.getText().toString().trim();
                    String quizNumberOfQuestion = binding.quizNumberOfQuestion.getText().toString().trim();
                    boolean checked = binding.QuizActive.isChecked();
                    Category selectedItem = (Category) binding.spinner.getSelectedItem();
                    Category category = null;

                    Long cId = 0L;
                    if (selectedItem != null) {
                        cId = selectedItem.getCid();
                        category = selectedItem;

                    }

                    if (quizTitle.isEmpty() || quizDescription.isEmpty() || quizMaxMarks.isEmpty() || quizNumberOfQuestion.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        Quiz quiz = new Quiz();
                        quiz.setTitle(quizTitle);
                        quiz.setDescription(quizDescription);
                        quiz.setMaxMarks(quizMaxMarks);
                        quiz.setNumberOfQuestions(quizNumberOfQuestion);
                        quiz.setActive(checked);
                        quiz.setCategory(category);

                        addQuiz(quiz);

                        FragmentManager fm = getParentFragmentManager();
                        fm.popBackStack();


                    }
                }
            });
        } else if (qid == -2) {

            categoryList.clear();
            getCategories();

            Quiz quiz = (Quiz) getArguments().getSerializable("QUIZ");

            binding.textView6.setVisibility(View.VISIBLE);
            binding.textView6.setText("Edit Quiz");
            binding.quizTitle.setVisibility(View.VISIBLE);
            binding.quizDescription.setVisibility(View.VISIBLE);
            binding.quizMaxMarks.setVisibility(View.VISIBLE);
            binding.quizNumberOfQuestion.setVisibility(View.VISIBLE);
            binding.spinner.setVisibility(View.VISIBLE);
            binding.QuizActive.setVisibility(View.VISIBLE);
            binding.addQuizBtn.setVisibility(View.VISIBLE);
            binding.addQuizBtn.setText("Edit Quiz");
            binding.textView7.setVisibility(View.VISIBLE);

            binding.quizTitle.setText(quiz.getTitle());
            binding.quizDescription.setText(quiz.getDescription());
            binding.quizMaxMarks.setText(quiz.getMaxMarks());
            binding.quizNumberOfQuestion.setText(quiz.getNumberOfQuestions());
            binding.QuizActive.setChecked(quiz.isActive());
            binding.spinner.setSelection(0);

            adapter = new SpinnerAdapter(categoryList, requireContext());
            binding.spinner.setAdapter(adapter);


            binding.addQuizBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String quizTitle = binding.quizTitle.getText().toString().trim();
                    String quizDescription = binding.quizDescription.getText().toString().trim();
                    String quizMaxMarks = binding.quizMaxMarks.getText().toString().trim();
                    String quizNumberOfQuestion = binding.quizNumberOfQuestion.getText().toString().trim();
                    boolean checked = binding.QuizActive.isChecked();
                    Category selectedItem = (Category) binding.spinner.getSelectedItem();
                    Category category = null;

                    Long cId = 0L;
                    if (selectedItem != null) {
                        cId = selectedItem.getCid();
                        category = selectedItem;

                    }

                    if (quizTitle.isEmpty() || quizDescription.isEmpty() || quizMaxMarks.isEmpty() || quizNumberOfQuestion.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        Quiz quiz1 = new Quiz();
                        quiz1.setTitle(quizTitle);
                        quiz1.setDescription(quizDescription);
                        quiz1.setMaxMarks(quizMaxMarks);
                        quiz1.setNumberOfQuestions(quizNumberOfQuestion);
                        quiz1.setActive(checked);
                        quiz1.setqId(quiz.getqId());

                     //   quiz1.setCategory(quiz.getCategory());

                         Category category1=new Category();
                         category1.setCid(cId);
                         quiz1.setCategory(category1);

                        updateQuiz(quiz1);
                        FragmentManager fm = getParentFragmentManager();
                        fm.popBackStack();


                    }
                }
            });


        } else {
            Question question = (Question) getArguments().getSerializable("question");
            if (question != null) {
                binding.textView5.setVisibility(View.VISIBLE);
                binding.textView5.setText("Edit Question");
                binding.qContent.setVisibility(View.VISIBLE);
                binding.option1.setVisibility(View.VISIBLE);
                binding.option2.setVisibility(View.VISIBLE);
                binding.option3.setVisibility(View.VISIBLE);
                binding.option4.setVisibility(View.VISIBLE);
                binding.answer.setVisibility(View.VISIBLE);
                binding.check.setVisibility(View.VISIBLE);
                binding.publish.setVisibility(View.VISIBLE);
                binding.addQuiestiontn.setVisibility(View.VISIBLE);
                binding.addQuiestiontn.setText("Edit question");

                binding.qContent.setText(question.getContent());
                binding.option1.setText(question.getOption1());
                binding.option2.setText(question.getOption2());
                binding.option3.setText(question.getOption3());
                binding.option4.setText(question.getOption4());
                binding.answer.setText(question.getAnswer());

                binding.addQuiestiontn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String content = binding.qContent.getText().toString().trim();
                        String option1 = binding.option1.getText().toString().trim();
                        String option2 = binding.option2.getText().toString().trim();
                        String option3 = binding.option3.getText().toString().trim();
                        String option4 = binding.option4.getText().toString().trim();
                        String answer = binding.answer.getText().toString().trim();
                        boolean checked = binding.check.isChecked();

                        if (content.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {

                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            Question question1 = new Question();
                            question1.setContent(content);
                            question1.setOption1(option1);
                            question1.setOption2(option2);
                            question1.setOption3(option3);
                            question1.setOption4(option4);
                            question1.setAnswer(answer);
                            question1.setQuesId(question.getQuesId());

                            Quiz quiz = new Quiz();
                            quiz.setqId(question.getQuiz().getqId());
                            question1.setQuiz(quiz);

                            updateQuestion(question1);
                            FragmentManager fm = getParentFragmentManager();
                            fm.popBackStack();
                        }


                    }
                });


            }
        }
        return binding.getRoot();
    }

    private void updateQuiz(Quiz quiz1) {
        sessionManager=new SessionManager(getContext());
        HandleProgressBar progressBar=new HandleProgressBar(binding.progressBar4);
        quizApiService=RetrofitClient.getClient().create(QuizApiService.class);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken!=null)
        {
            progressBar.show();
            Call<Quiz> quizCall = quizApiService.updateQuiz("Bearer " + accessToken, quiz1);
            quizCall.enqueue(new Callback<Quiz>() {
                @Override
                public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                    if (response.isSuccessful() && response.body()!=null)
                    {
                        Quiz body = response.body();
                        Toast.makeText(context, "Quiz Updated", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                    else
                    {
                        progressBar.dismiss();
                        Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Quiz> call, Throwable t) {

                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            progressBar.dismiss();
            Toast.makeText(context, "Token is empty", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateQuestion(Question question1) {

        sessionManager = new SessionManager(getContext());
        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar4);
        apiService = RetrofitClient.getClient().create(QuestionApiService.class);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            progressBar.show();
            Call<Question> questionCall = apiService.updateQuestion("Bearer " + accessToken, question1);
            questionCall.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(context, "Question Updated", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    } else {
                        Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });
        } else {
            progressBar.dismiss();
            Toast.makeText(context, "Token is empty", Toast.LENGTH_SHORT).show();
        }


    }

    private void addQuiz(Quiz quiz) {

        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar4);
        progressBar.show();
        sessionManager = new SessionManager(getContext());
        quizApiService = RetrofitClient.getClient().create(QuizApiService.class);
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            Call<Quiz> quizCall = quizApiService.addQuiz("Bearer " + accessToken, quiz);

            quizCall.enqueue(new Callback<Quiz>() {
                @Override
                public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(context, "Quiz Added..", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Quiz> call, Throwable t) {

                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Token is null", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }

    }

    private void getCategories() {
        sessionManager = new SessionManager(getContext());
        categoryApiService = RetrofitClient.getClient().create(CategoryApiService.class);

        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null) {
            Call<List<Category>> call = categoryApiService.getAllCategories("Bearer " + accessToken);

            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        List<Category> body = response.body();

                        for (Category c : body) {
                            categoryList.add(c);
                            Log.d("Category", c.title);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                    Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Token is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void addQuestion(Question question) {

        HandleProgressBar progressBar = new HandleProgressBar(binding.progressBar4);
        progressBar.show();

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(QuestionApiService.class);

        String accessToken = sessionManager.getAccessToken();

        if (accessToken != null) {
            Call<Question> questionCall = apiService.addQuestion("Bearer " + accessToken, question);

            questionCall.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        progressBar.dismiss();
                        Toast.makeText(context, "Question Added..", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.dismiss();
                        Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            progressBar.dismiss();
            Toast.makeText(context, "Token is null", Toast.LENGTH_SHORT).show();
        }

    }

}