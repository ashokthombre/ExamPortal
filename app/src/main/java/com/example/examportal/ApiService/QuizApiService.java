package com.example.examportal.ApiService;

import com.example.examportal.model.exam.Category;
import com.example.examportal.model.exam.Quiz;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuizApiService {

    @GET("/quiz/")
    Call<List<Quiz>> getAllQuiz(@Header("Authorization") String token);

    @GET("quiz/category/{cid}")
    Call<List<Quiz>>getQuizzesByCategory(@Header("Authorization") String token,@Path("cid") Long cid);

    @POST("/quiz/")
    Call<Quiz> addQuiz(@Header("Authorization") String token, @Body Quiz quiz);

    @DELETE("/quiz/{qId}")
    Call <Void> deleteQuiz(@Header("Authorization") String token, @Path("qId") Long qId);

    @PUT("quiz/")
    Call<Quiz> updateQuiz(@Header("Authorization") String token,@Body Quiz quiz);

}
