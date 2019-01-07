package com.supinfo.and.suptodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.TodoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends BaseActivity {

    EditText friendUserText;
    EditText todoText;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button btnCancel = (Button) findViewById(R.id.cancelbtnAdd);
        Button btnAdd = (Button) findViewById(R.id.addBtnAdd);
        friendUserText = (EditText) findViewById(R.id.shareWithAdd);
        todoText = (EditText) findViewById(R.id.listDisplayAdd);

        loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);

        btnCancel.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v ->
                APIInstance.shareWithFriend(this,loggedUser.getUsername(), loggedUser.getPassword(), friendUserText.getText().toString(), wasShared -> {
                    if(wasShared){
                        APIInstance.listFromAPI(this,loggedUser.getUsername(), loggedUser.getPassword(), todoResponses -> {
                             int id = Integer.parseInt(todoResponses.get(todoResponses.size()-1).getId()) ;
                             String finalTodo =loggedUser.getUsername() + " -> " + friendUserText.getText().toString() + "\n" + todoText.getText().toString();
                            APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword(),id,finalTodo);
                             finish();
                        });
                    }
                })
        );

    }


}
