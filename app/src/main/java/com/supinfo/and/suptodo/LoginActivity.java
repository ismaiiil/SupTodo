package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private EditText editUserNameLog;
    private EditText editPasswordLog;
    private Button btnLogin;
    private Button btnRegisterIns;

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
            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
            startActivity(intent);
        }
    }

    public void returnToPreviousActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}

