package com.supinfo.and.suptodo;

import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import com.supinfo.and.suptodo.SQLITE.User;

public class AddActivity extends BaseActivity {

    EditText friendUserText;
    EditText todoText;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Starting with API 26, findViewById uses inference for its return type, so you no longer have to cast.
        Button btnCancel = findViewById(R.id.cancelbtnAdd);
        Button btnAdd = findViewById(R.id.addBtnAdd);
        friendUserText = findViewById(R.id.shareWithAdd);
        todoText = findViewById(R.id.listDisplayAdd);

        loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);

        btnCancel.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v ->
                APIInstance.shareWithFriend(this,loggedUser.getUsername(), loggedUser.getPassword(), friendUserText.getText().toString(), wasShared -> {
                    if(wasShared){
                        addUsernameToTODO();
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

    private void addUsernameToTODO() {
        APIInstance.listFromAPI(this,loggedUser.getUsername(), loggedUser.getPassword(), todoResponses -> {
             int id = Integer.parseInt(todoResponses.get(todoResponses.size()-1).getId()) ;
             String finalTodo =loggedUser.getUsername() + " -> " + friendUserText.getText().toString() + "\n" + todoText.getText().toString();
            APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword(),id,finalTodo);
             finish();
        });
    }

    @Override
    public void onBackPressed() {finish();}

}
