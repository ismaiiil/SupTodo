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
    String toastWhenIncomplete = "Try Again";
    private Button btnRegisterIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserNameLog = (EditText)findViewById(R.id.userNameLog);
        editPasswordLog = (EditText)findViewById(R.id.passwordLog);
        btnLogin = (Button)findViewById(R.id.loginBtn);
        btnRegisterIns = (Button)findViewById(R.id.registerInsBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateIfEmpty(editUserNameLog.getText().toString(), editPasswordLog.getText().toString());
            }
        });

        btnRegisterIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToPreviousActivity();
            }
        });


    }

    public void validateIfEmpty(String userNameLog, String passwordLog){
        if((userNameLog.isEmpty())|| (passwordLog.isEmpty())){
            Toast.makeText(getApplicationContext(),toastWhenIncomplete,Toast.LENGTH_LONG).show();

        }else{
            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
            startActivity(intent);
        }
    }

    public void returnToPreviousActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}

