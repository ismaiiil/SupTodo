package com.supinfo.and.suptodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;
import com.supinfo.and.suptodo.remote.ApiUtil;
import com.supinfo.and.suptodo.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userService = ApiUtil.getUserService();
        testLogin();
        testList();

    }

    private void testLogin(){
        Call<UserResponse> call = userService.login("userone","test");
        System.out.println("calling api");
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                System.out.println(response.body().getFirstname());
                System.out.println("finised calling api");
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println(t);
                System.out.println("finised calling api");
            }

        });

    }

    private void testList(){
        Call<List<TodoResponse>> call = userService.list("userone","test");
        System.out.println("calling api");
        call.enqueue(new Callback<List<TodoResponse>>() {
            @Override
            public void onResponse(Call<List<TodoResponse>> call, Response<List<TodoResponse>> response) {
                for(TodoResponse todoResponse:response.body()){
                    System.out.println(todoResponse.getId() + " " + todoResponse.getLastupdate());
                }
                System.out.println("finised calling api");
            }

            @Override
            public void onFailure(Call<List<TodoResponse>> call, Throwable t) {
                System.out.println(t);
                System.out.println("finised calling api");
            }

        });

    }
}
