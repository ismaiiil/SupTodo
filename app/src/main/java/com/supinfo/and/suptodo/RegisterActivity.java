package com.supinfo.and.suptodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.R;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;
import com.supinfo.and.suptodo.API.ApiUtil;
import com.supinfo.and.suptodo.API.UserService;

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
        testRegister();
        testLogin();
        testList();
        testRead();
        testUpdate();
        testRead();
        testShare();
        testLogout();

    }

    private void testShare() {
        Call<JsonObject> call = userService.share("userone","test","usertwo");
        System.out.println("calling api from testShare");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testShare");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not shared: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was shared: " +stateResponse.isSuccess());
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }

        });
    }

    private void testUpdate() {
        Call<JsonObject> call = userService.update("userone","test",572,"the first todo \n the 2nd todo");
        System.out.println("calling api from testUpdate");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testUpdate");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not updated: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was updated: " +stateResponse.isSuccess());
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }

        });
    }

    private void testRead() {
        Call<JsonObject> call = userService.read("userone","test",572);
        System.out.println("calling api from testRead");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testRead");
                if (response.body().has("id")){
                    TodoResponse todoResponse = gson.fromJson(response.body(),TodoResponse.class);
                    System.out.println("the todo is with id " + todoResponse.getId() + " and text " + todoResponse.getTodo());
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not read: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    System.out.println("server rejected this action " +stateResponse.isSuccess());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }

        });


    }

    private void testLogin(){
        Call<JsonObject> call = userService.login("userone","test");
        System.out.println("calling api from testLogin");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testLogin");
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    System.out.println("the user has name: " + userResponse.getFirstname() + " " + userResponse.getLastname());
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("login failed: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    System.out.println("server rejected this action " + stateResponse.isSuccess());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }

        });

    }

    private void testLogout(){
        Call<StateResponse> call = userService.logout();
        System.out.println("calling api from testLogout");
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                System.out.println("finished calling API from testLogout");
                System.out.println("You were sucessfully logged out: " + response.body().isSuccess());
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }
        });
    }

    private void testRegister(){
        Call<JsonObject> call = userService.register("userten","test","jeff",
                "maname","test@mail.com");
        System.out.println("calling api from testRegister");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testRegister");
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    System.out.println("the user has name: " + userResponse.getFirstname() + " " + userResponse.getLastname() + " was registered and logged in");
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("You were not registered since: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    System.out.println("You were not registered: "+stateResponse.isSuccess());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }
        });
    }

    private void testList(){
        Call<List<TodoResponse>> call = userService.list("userone","test");
        System.out.println("calling api from testList");
        call.enqueue(new Callback<List<TodoResponse>>() {
            @Override
            public void onResponse(Call<List<TodoResponse>> call, Response<List<TodoResponse>> response) {
                System.out.println("finished calling API from testList");
                for(TodoResponse todoResponse:response.body()){
                    System.out.print(todoResponse.getId() + " " + todoResponse.getLastupdate() + " " + todoResponse.getTodo() + ", ");
                }
                System.out.println();

            }

            @Override
            public void onFailure(Call<List<TodoResponse>> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
            }

        });

    }


}