package com.example.examportal.ApiService;

import com.example.examportal.model.User;
import com.example.examportal.model.exam.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryApiService {

    @GET("/category/")
    Call<List<Category>> getAllCategories(@Header("Authorization") String token);

    @POST("/category/")
    Call<Category> addCategory(@Header("Authorization") String token, @Body Category category);

    @PUT("/category/")
    Call<Category> updateCategory(@Header("Authorization") String token,@Body Category category);

    @DELETE("/category/{cid}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("cid") Long cid);


}
