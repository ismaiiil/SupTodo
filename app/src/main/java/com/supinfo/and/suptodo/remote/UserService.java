package com.supinfo.and.suptodo.remote;

import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("?action=login")
    Call<UserResponse> login(@Query("username") String username, @Query("password") String password);

    @GET("?action=list")
    Call<List<TodoResponse>> list(@Query("username") String username, @Query("password") String password);
}
