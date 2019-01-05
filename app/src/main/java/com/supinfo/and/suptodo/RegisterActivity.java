package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

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
            Intent intent = new Intent(RegisterActivity.this, ToDoListActivity.class);
            startActivity(intent);
        }
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
