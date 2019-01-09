package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends BaseActivity {

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
        setTitle(R.string.title_activity_register);

        getUIElements();

        btnRegister.setOnClickListener((v) ->
                validateFields(editUserName.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString())
        );

        btnLoginInstead.setOnClickListener(v ->
                openLoginActivity()
        );


    }


    private void getUIElements() {
        editUserName = findViewById(R.id.UserName);
        editFirstName = findViewById(R.id.FirstName);
        editLastName = findViewById(R.id.LastName);
        editEmail = findViewById(R.id.Email);
        editPassword = findViewById(R.id.Password);
        btnRegister = findViewById(R.id.registerBtn);
        btnLoginInstead = findViewById(R.id.loginInsBtn);
    }

    public void validateFields(String userName, String firstName, String lastName, String email, String password){
        if((userName.trim().isEmpty())|| (firstName.trim().isEmpty()) || (lastName.trim().isEmpty()) || (email.trim().isEmpty()) || (password.isEmpty())){
            Toast.makeText(getApplicationContext(),"You cannot leave empty fields",Toast.LENGTH_LONG).show();

        }else{
            APIInstance.registerUser(this,userName,password,firstName,lastName,email,this);
        }
    }



    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
