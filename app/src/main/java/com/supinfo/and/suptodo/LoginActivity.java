package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.API.ApiUtil;
import com.supinfo.and.suptodo.API.UserService;
import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    private EditText editUserNameLog;
    private EditText editPasswordLog;
    private Button btnLogin;
    private Button btnRegisterIns;

    UserService userService = ApiUtil.getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);

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
        if((userNameLog.trim().isEmpty())|| (passwordLog.trim().isEmpty())){
            Toast.makeText(getApplicationContext(),"You cannot leave empty fields",Toast.LENGTH_LONG).show();

        }else{
            loginUser(new User(userNameLog,passwordLog));
        }
    }


    public void returnToPreviousActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}

