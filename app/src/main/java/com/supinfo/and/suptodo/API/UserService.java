package com.supinfo.and.suptodo.API;

import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.TodoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("?action=register")
    Call<JsonObject> register(@Query("username") String username, @Query("password") String password
            , @Query("firstname") String firstname, @Query("lastname") String lastname
            , @Query("email") String email);

    @GET("?action=login")
    Call<JsonObject> login(@Query("username") String username, @Query("password") String password);

    @GET("?action=logout")
    Call<StateResponse> logout();

    @GET("?action=list")
    Call<List<TodoResponse>> list(@Query("username") String username, @Query("password") String password);

    @GET("?action=read")
    Call<JsonObject> read(@Query("username") String username, @Query("password") String password
            , @Query("id") int id);

    @GET("?action=update")
    Call<JsonObject> update(@Query("username") String username, @Query("password") String password
            , @Query("id") int id , @Query("todo") String todo);

    @GET("?action=share")
    Call<JsonObject> share(@Query("username") String username, @Query("password") String password
            , @Query("user") String userFriend);

}
