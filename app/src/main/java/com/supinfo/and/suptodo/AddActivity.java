package com.supinfo.and.suptodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        if(todoText.getText().toString().isEmpty()){
            todoText.append(" + ");
        }
        todoText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        todoText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        todoText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                int start = Math.max(todoText.getSelectionStart(), 0);
                int end = Math.max(todoText.getSelectionEnd(), 0);
                todoText.getText().replace(Math.min(start, end), Math.max(start, end),
                        "\n + ", 0, "\n + ".length());
            }
            return false;
        });
    }

    //do this to windows you want back button to behave normally
    @Override
    public void onBackPressed() {finish();}

}
