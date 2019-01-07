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

    public void loginUser(BaseActivity baseActivity, User user, CompletionHandlers.MyLoginCompletionHandler mlCompletionHandler){
        baseActivity.showProgressDialog("Logging in");
        Call<JsonObject> call = userService.login(user.getUsername(),user.getPassword());
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    Intent intent = new Intent(baseActivity, ToDoListActivity.class);
                    intent.putExtra(baseActivity.LOGGED_USER_KEY,userResponse);
                    baseActivity.startActivity(intent);
                    baseActivity.hideProgressDialog();
                    mlCompletionHandler.onSucceeded(true);
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    baseActivity.hideProgressDialog();
                    makeToast(baseActivity,"invalid credentials: " + messageResponse.getMessage());
                    mlCompletionHandler.onSucceeded(false);
                }else{
                    makeToast(baseActivity,"invalid info provided");
                    baseActivity.hideProgressDialog();
                    mlCompletionHandler.onSucceeded(false);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                makeToast(baseActivity,"Something went wrong when trying to connect to the server");
                baseActivity.hideProgressDialog();
                mlCompletionHandler.onFailed();
            }

        });
    }

    public void registerUser(BaseActivity baseActivity, String userName,String password,String firstName,
                                String lastName,String email,Activity caller){
        baseActivity.showProgressDialog("Registering");
        Call<JsonObject> call = userService.register(userName,password,firstName,
                lastName,email);
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    //add registered user to SQLITE
                    baseActivity.deleteAll(() -> baseActivity.insert(new User(userResponse.getUsername(),userResponse.getPassword())));
                    Intent intent = new Intent(baseActivity, ToDoListActivity.class);
                    intent.putExtra(baseActivity.LOGGED_USER_KEY,userResponse);
                    baseActivity.startActivity(intent);
                    baseActivity.hideProgressDialog();
                    caller.finish();
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    makeToast(baseActivity,"You were not registered since: " + messageResponse.getMessage());
                    baseActivity.hideProgressDialog();
                }else{
                    makeToast(baseActivity,"invalid info provided");
                    baseActivity.hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                makeToast(baseActivity,"Something went wrong when trying to connect to the server");
                baseActivity.hideProgressDialog();
            }
        });
    }

    public void logoutUser(BaseActivity baseActivity,Activity caller){
        baseActivity.showProgressDialog("Logging Out");
        Call<StateResponse> call = userService.logout();
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                baseActivity.hideProgressDialog();
                baseActivity.startRegisterActivity();
                baseActivity.deleteAll(null);
                caller.finish();
                makeToast(baseActivity,"You were sucessfully logged out: " + response.body().isSuccess());
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                makeToast(baseActivity,"Something went wrong when trying to connect to the server");
                baseActivity.startRegisterActivity();
                baseActivity.deleteAll(null);
                caller.finish();
            }
        });
    }

    public void listFromAPI(BaseActivity baseActivity,String username,String password, CompletionHandlers.MyTodoListCompletionHandler completionHandler){
        baseActivity.showProgressDialog("Refreshing");
        Call<List<TodoResponse>> call = userService.list(username,password);
        call.enqueue(new Callback<List<TodoResponse>>() {
            @Override
            public void onResponse(Call<List<TodoResponse>> call, Response<List<TodoResponse>> response) {
                completionHandler.onFinished(response.body());
                baseActivity.hideProgressDialog();

            }
            @Override
            public void onFailure(Call<List<TodoResponse>> call, Throwable t) {
                System.out.println(t);
                makeToast(baseActivity,"Something went wrong when trying to fetch the list from the server!");

                baseActivity.hideProgressDialog();
            }
        });

    }




    public void shareWithFriend(BaseActivity baseActivity,String username,String password,String userFriend, CompletionHandlers.ShareCompletionHandler shareCH) {
        baseActivity.showProgressDialog("Adding your friend");
        Call<JsonObject> call = userService.share(username,password,userFriend);
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    makeToast(baseActivity,"the todo was not shared: " + messageResponse.getMessage());
                    shareCH.onFinished(false);
                    baseActivity.hideProgressDialog();
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        makeToast(baseActivity,"the todo was shared: " +stateResponse.isSuccess());
                        shareCH.onFinished(true);
                        baseActivity.hideProgressDialog();
                    }else{
                        makeToast(baseActivity,"server rejected this action " +stateResponse.isSuccess());
                        shareCH.onFinished(false);
                        baseActivity.hideProgressDialog();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                makeToast(baseActivity,"Something went wrong when trying to connect to the server");
                baseActivity.hideProgressDialog();
            }

        });
    }


    public void readToDoByID(BaseActivity baseActivity, String username, String password, int id, CompletionHandlers.MyReadTodoCompletionHandler myReadTodoCompletionHandler) {
        Call<JsonObject> call = userService.read(username,password,id);
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("id")){
                    TodoResponse todoResponse = gson.fromJson(response.body(),TodoResponse.class);
                    makeToast(baseActivity,"the todo is with id " + todoResponse.getId() + " was fetched ");
                    myReadTodoCompletionHandler.onFinished(todoResponse);
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    makeToast(baseActivity,"the todo was not fetched: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    makeToast(baseActivity,"server rejected this action " +stateResponse.isSuccess());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                makeToast(baseActivity,"Something went wrong when trying to connect to the server");
            }

        });


    }


    public void updateTodoByID(BaseActivity baseActivity,String username,String password, int id, String todo) {
        Toast.makeText(baseActivity.getApplicationContext(),"Updating your todo!",Toast.LENGTH_SHORT).show();
        Call<JsonObject> call = userService.update(username,password,id,todo);
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    makeToast(baseActivity,"the todo was not updated: " + messageResponse.getMessage());
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        makeToast(baseActivity,"the todo was updated: " +stateResponse.isSuccess());
                    }else{
                        makeToast(baseActivity,"server rejected this action " +stateResponse.isSuccess());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               makeToast(baseActivity,"Something went wrong when trying to connect to the server");

                baseActivity.hideProgressDialog();
            }

        });
    }

    public void makeToast(Activity activity,String message){
        Toast.makeText(activity.getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}


