package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.R;
import com.supinfo.and.suptodo.SQLITE.User;
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

public class RegisterActivity extends BaseActivity {

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
            APIInstance.registerUser(this,userName,password,firstName,lastName,email,this);
        }
    }



    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
