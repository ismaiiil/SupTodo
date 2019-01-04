package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUserName;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
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
        btnLoginInstead = (Button)findViewById(R.id.loginInsBtn);

        btnLoginInstead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                openLoginActivity();
            }

        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
