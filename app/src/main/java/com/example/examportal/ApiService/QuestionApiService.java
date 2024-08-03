package com.example.examportal.ApiService;

import com.example.examportal.model.exam.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuestionApiService {


    @POST("/question/")
    Call<Question> addQuestion(@Header("Authorization") String token, @Body Question question);

    @GET("/question/quiz/{qid}")
    Call<List<Question>> getAllQuestion(@Header("Authorization") String token, @Path("qid") Long qid);

    @PUT("/question/")
   Call<Question> updateQuestion(@Header("Authorization")String token,@Body Question question);

    @DELETE("/question/{qid}")
    Call<Void> deleteQuestion(@Header("Authorization") String token,@Path("qid") Long qid);

}
