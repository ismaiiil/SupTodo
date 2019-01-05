package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    UserService userService = ApiUtil.getUserService();

    private EditText editUserName;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private Button btnRegister;
    private Button btnLoginInstead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        testRegister();
        testList();
        testRead();
        testUpdate();
        testRead();
        testShare();
        testLogout();


        getUIElements();

        btnRegister.setOnClickListener((v) ->
                validateFields(editUserName.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString())
        );

        btnLoginInstead.setOnClickListener(v ->
                openLoginActivity()
        );


    }

    private void getUIElements() {
        editUserName = (EditText)findViewById(R.id.UserName);
        editFirstName = (EditText)findViewById(R.id.FirstName);
        editLastName = (EditText)findViewById(R.id.LastName);
        editEmail = (EditText)findViewById(R.id.Email);
        editPassword = (EditText)findViewById(R.id.Password);
        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnLoginInstead = (Button)findViewById(R.id.loginInsBtn);
    }

    public void validateFields(String userName, String firstName, String lastName, String email, String password){
        if((userName.trim().isEmpty())|| (firstName.trim().isEmpty()) || (lastName.trim().isEmpty()) || (email.trim().isEmpty()) || (password.isEmpty())){
            Toast.makeText(getApplicationContext(),"Incomplete form",Toast.LENGTH_LONG).show();

        }else{
            Call<JsonObject> call = userService.register(userName,password,firstName,
                    lastName,email);
            System.out.println("calling api from testRegister");
            call.enqueue(new Callback<JsonObject>() {
                Gson gson = new Gson();
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    System.out.println("finished calling API from testRegister");
                    if (response.body().has("id")){
                        UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                        System.out.println("the user has name: " + userResponse.getFirstname() + " " + userResponse.getLastname() + " was registered and logged in");
                        Intent intent = new Intent(RegisterActivity.this, ToDoListActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.body().has("message")){
                        MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                        String message = "You were not registered since: " + messageResponse.getMessage();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }else{
                        //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                        String message = "invalid info provided";
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println(t);
                    System.out.println("Something went wrong when trying to connect to the server");
                }
            });

        }
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



    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
