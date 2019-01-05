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
    String toastWhenInvalid = "Incomplete form";
   // private int counter = 3;
    private Button btnLoginInstead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUserName = (EditText)findViewById(R.id.UserName);
        editFirstName = (EditText)findViewById(R.id.FirstName);
        editLastName = (EditText)findViewById(R.id.LastName);
        editEmail = (EditText)findViewById(R.id.Email);
        editPassword = (EditText)findViewById(R.id.Password);
        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnLoginInstead = (Button)findViewById(R.id.loginInsBtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateIfEmpty(editUserName.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
            }
        });

        btnLoginInstead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                openLoginActivity();
            }

        });


    }

    public void validateIfEmpty(String userName, String firstName, String lastName, String email, String password){
        if((userName.isEmpty())|| (firstName.isEmpty()) || (lastName.isEmpty()) || (email.isEmpty()) || (password.isEmpty())){
            Toast.makeText(getApplicationContext(),toastWhenInvalid,Toast.LENGTH_LONG).show();

        }else{
            Intent intent = new Intent(RegisterActivity.this, ToDoListActivity.class);
            startActivity(intent);
        }
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
