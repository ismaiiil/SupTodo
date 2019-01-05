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
import com.supinfo.and.suptodo.API.ApiUtil;
import com.supinfo.and.suptodo.API.UserService;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText editUserNameLog;
    private EditText editPasswordLog;
    private Button btnLogin;
    private Button btnRegisterIns;

    UserService userService = ApiUtil.getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getUIElements();

        btnLogin.setOnClickListener(v -> validateFields(editUserNameLog.getText().toString(), editPasswordLog.getText().toString()));

        btnRegisterIns.setOnClickListener(v -> returnToPreviousActivity());


    }

    private void getUIElements() {
        editUserNameLog = (EditText)findViewById(R.id.userNameLog);
        editPasswordLog = (EditText)findViewById(R.id.passwordLog);
        btnLogin = (Button)findViewById(R.id.loginBtn);
        btnRegisterIns = (Button)findViewById(R.id.registerInsBtn);
    }

    public void validateFields(String userNameLog, String passwordLog){
        if((userNameLog.isEmpty())|| (passwordLog.isEmpty())){
            Toast.makeText(getApplicationContext(),"You cannot leave empty fields",Toast.LENGTH_LONG).show();

        }else{
            Call<JsonObject> call = userService.login(userNameLog,passwordLog);
            call.enqueue(new Callback<JsonObject>() {
                Gson gson = new Gson();
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("id")){
                        UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                        Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.body().has("message")){
                        MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                        String message = "login failed: " + messageResponse.getMessage();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }else{
                        //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                        String message = "server rejected this action ";
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


    public void returnToPreviousActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}

