package com.supinfo.and.suptodo.API;

import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.model.TodoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("?action=login")
    Call<JsonObject> login(@Query("username") String username, @Query("password") String password);

    @GET("?action=list")
    Call<List<TodoResponse>> list(@Query("username") String username, @Query("password") String password);
}
