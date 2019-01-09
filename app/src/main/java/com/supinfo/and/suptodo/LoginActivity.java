package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.supinfo.and.suptodo.API.CompletionHandlers;
import com.supinfo.and.suptodo.SQLITE.User;


public class LoginActivity extends BaseActivity {

    private EditText editUserNameLog;
    private EditText editPasswordLog;
    private Button btnLogin;
    private Button btnRegisterIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);

        getUIElements();
        btnLogin.setOnClickListener(v -> validateFields(editUserNameLog.getText().toString(), editPasswordLog.getText().toString()));

        btnRegisterIns.setOnClickListener(v -> returnToPreviousActivity());

        User loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);
        if(loggedUser != null){
            editUserNameLog.setText(loggedUser.getUsername());
            editPasswordLog.setText(loggedUser.getPassword());
        }

    }

    private void getUIElements() {
        editUserNameLog = findViewById(R.id.userNameLog);
        editPasswordLog = findViewById(R.id.passwordLog);
        btnLogin = findViewById(R.id.loginBtn);
        btnRegisterIns = findViewById(R.id.registerInsBtn);
    }

    public void validateFields(String userNameLog, String passwordLog){
        if((userNameLog.trim().isEmpty())|| (passwordLog.trim().isEmpty())){
            Toast.makeText(getApplicationContext(),"You cannot leave empty fields",Toast.LENGTH_LONG).show();

        }else{
            APIInstance.loginUser(this, new User(userNameLog, passwordLog), new CompletionHandlers.MyLoginCompletionHandler() {
                @Override
                public void onSucceeded(Boolean wasAuthenticated) {
                    if(wasAuthenticated){
                        userValidated(userNameLog,passwordLog);
                    }
                }
                @Override
                public void onFailed() {}
            });
        }
    }

    private void userValidated(String userNameLog, String passwordLog){
        deleteAll(() -> insert(new User(userNameLog,passwordLog)));
        this.finish();
    }

    public void returnToPreviousActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}

