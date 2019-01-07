package com.supinfo.and.suptodo.API;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.BaseActivity;
import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.ToDoListActivity;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICallHandler {
    public static APICallHandler INSTANCE;
    private UserService userService = ApiUtil.getUserService();

    public static APICallHandler getINSTANCE(){
        if (INSTANCE == null) {
            INSTANCE = new APICallHandler();
        }
        return INSTANCE;
    }

    public void loginUser(BaseActivity baseActivity,User user, Activity caller ){
        Call<JsonObject> call = userService.login(user.getUsername(),user.getPassword());
        baseActivity.showProgressDialog("Logging in");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    Intent intent = new Intent(baseActivity, ToDoListActivity.class);
                    intent.putExtra(baseActivity.LOGGED_USER_KEY,userResponse);
                    //add logged user to SQLITE
                    baseActivity.deleteAllAndInsert(new User(userResponse.getUsername(),userResponse.getPassword()));
                    baseActivity.startActivity(intent);
                    baseActivity.hideProgressDialog();
                    if(caller != null){
                        caller.finish();
                    }
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    baseActivity.hideProgressDialog();
                    String message = "invalid credentials: " + messageResponse.getMessage();
                    Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }else{
                    //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    String message = "invalid info provided";
                    baseActivity.hideProgressDialog();
                    Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                baseActivity.hideProgressDialog();
                caller.finishAffinity();
            }

        });
    }

    public void registerUser(BaseActivity baseActivity, String userName,String password,String firstName,
                                String lastName,String email,Activity caller){
        Call<JsonObject> call = userService.register(userName,password,firstName,
                lastName,email);
        System.out.println("calling api from testRegister");
        baseActivity.showProgressDialog("Registering");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testRegister");
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    System.out.println("the user has name: " + userResponse.getFirstname() + " " + userResponse.getLastname() + " was registered and logged in");
                    //add registered user to SQLITE
                    baseActivity.deleteAllAndInsert(new User(userResponse.getUsername(),userResponse.getPassword()));
                    Intent intent = new Intent(baseActivity, ToDoListActivity.class);
                    intent.putExtra(baseActivity.LOGGED_USER_KEY,userResponse);
                    baseActivity.startActivity(intent);
                    baseActivity.hideProgressDialog();
                    caller.finish();
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    String message = "You were not registered since: " + messageResponse.getMessage();
                    baseActivity.hideProgressDialog();
                    Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }else{
                    //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    String message = "invalid info provided";
                    baseActivity.hideProgressDialog();
                    Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                baseActivity.hideProgressDialog();
            }
        });
    }

    public void logoutUser(BaseActivity baseActivity,Activity caller){
        baseActivity.showProgressDialog("Logging Out");
        Call<StateResponse> call = userService.logout();
        System.out.println("calling api from testLogout");
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                baseActivity.hideProgressDialog();
                System.out.println("finished calling API from testLogout");
                baseActivity.startRegisterActivity();
                baseActivity.deleteAll();
                caller.finish();
                String message = "You were sucessfully logged out: " + response.body().isSuccess();
                Toast.makeText(caller.getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                baseActivity.startRegisterActivity();
                baseActivity.deleteAll();
                Toast.makeText(baseActivity.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                caller.finish();
            }
        });
    }

    public void listFromAPI(BaseActivity baseActivity,String username,String password, CompletionHandlers.MyTodoListCompletionHandler myListener){
        baseActivity.showProgressDialog("Refreshing");
        Call<List<TodoResponse>> call = userService.list(username,password);
        System.out.println("calling api from testList");
        call.enqueue(new Callback<List<TodoResponse>>() {
            @Override
            public void onResponse(Call<List<TodoResponse>> call, Response<List<TodoResponse>> response) {
                myListener.onFinished(response.body());
                baseActivity.hideProgressDialog();

            }
            @Override
            public void onFailure(Call<List<TodoResponse>> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(baseActivity.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                baseActivity.hideProgressDialog();
            }
        });

    }




    public void shareWithFriend(BaseActivity baseActivity,String username,String password,String userFriend, CompletionHandlers.ShareCompletionHandler shareCH) {
        baseActivity.showProgressDialog("Adding your friend");
        Call<JsonObject> call = userService.share(username,password,userFriend);
        System.out.println("calling api from testShare");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testShare");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not shared: " + messageResponse.getMessage());
                    shareCH.onFinished(false);
                    baseActivity.hideProgressDialog();
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was shared: " +stateResponse.isSuccess());
                        shareCH.onFinished(true);
                        baseActivity.hideProgressDialog();
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                        shareCH.onFinished(false);
                        baseActivity.hideProgressDialog();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
                baseActivity.hideProgressDialog();
            }

        });
    }




    public void updateTodoByID(BaseActivity baseActivity,String username,String password, int id, String todo) {
        baseActivity.showProgressDialog("Updating your todo!");
        Call<JsonObject> call = userService.update(username,password,id,todo);
        System.out.println("calling api from testUpdate");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testUpdate");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not updated: " + messageResponse.getMessage());
                    baseActivity.hideProgressDialog();
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was updated: " +stateResponse.isSuccess());
                        baseActivity.hideProgressDialog();
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                        baseActivity.hideProgressDialog();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
                baseActivity.hideProgressDialog();
            }

        });
    }
}


