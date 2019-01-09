package com.supinfo.and.suptodo;

import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.TodoResponse;

public class EditActivity extends BaseActivity {

    EditText todoList;
    EditText userSharedWith;
    TextView editTextInfo;
    TextView title;
    User loggedUser;
    Boolean isEditing = false;
    Boolean isPaused = false;
    TodoResponse intentToDoResponse;

    Handler handler;
    Runnable runnableCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getUIElements();
        getIntentData();
        setFields();
        if(intentToDoResponse.getUserinvited() != null){
            automateUpdateView();
        }else{
            editTextInfo.setText(R.string.EditPrivateTODOInfo);

        }
        Button updateBtn = findViewById(R.id.updateBtnEdit);
        updateBtn.setOnClickListener(v -> {
            APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                    ,Integer.parseInt(intentToDoResponse.getId()), userSharedWith.getText() + "\n" + todoList.getText().toString() );
            isEditing = false;
        });
        Button cancelBtn = findViewById(R.id.cancelBtnEdit);
        cancelBtn.setOnClickListener(v -> finish());
        setToDoListActionEvent();


    }

    private void getIntentData() {
        intentToDoResponse = (TodoResponse) getIntent().getSerializableExtra(PASSED_TODO);
        loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);
    }

    private void getUIElements() {
        todoList = findViewById(R.id.listDisplayEdit);
        userSharedWith = findViewById(R.id.editShared);
        editTextInfo = findViewById(R.id.textInfoEdit);
        title = findViewById(R.id.textTitle);
    }

    private void setToDoListActionEvent(){
        todoList.setImeOptions(EditorInfo.IME_ACTION_DONE);
        todoList.setRawInputType(InputType.TYPE_CLASS_TEXT);
        todoList.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                int start = Math.max(todoList.getSelectionStart(), 0);
                int end = Math.max(todoList.getSelectionEnd(), 0);
                todoList.getText().replace(Math.min(start, end), Math.max(start, end),
                        "\n + ", 0, "\n + ".length());
                APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                        ,Integer.parseInt(intentToDoResponse.getId()), userSharedWith.getText() + "\n" + todoList.getText().toString() );
                isEditing = false;
            }
            System.out.println(actionId);
            return false;
        });
    }

    private void setFields(){
        String totalString = intentToDoResponse.getTodo();

        if(totalString.contains("\n")){
            String[] result = totalString.split("\n", 2);
            String ownerString = result[0];
            String todoString = result[1];
            userSharedWith.setText(ownerString);
            todoList.setText(todoString);
        }else{
            userSharedWith.setText("");
            todoList.setText(totalString);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacks(runnableCode);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        if(!isEditing){
            updateView();
        }

    }

    private void automateUpdateView(){
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                if(!isEditing && !isPaused){
                    System.out.println("not editing and not paused the text has been updated");
                    updateView();
                }else{
                    System.out.println("paused or editing not updated");
                }
                handler.postDelayed(this, 30000);
            }
        };
        handler.post(runnableCode);
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEditing = todoList.getTag() == null;
            }

        };
        todoList.addTextChangedListener(textWatcher);
    }

    protected void updateView(){
        APIInstance.readToDoByID(this, loggedUser.getUsername(), loggedUser.getPassword()
                , Integer.parseInt(intentToDoResponse.getId()), todoResponse -> {
                    String totalString = todoResponse.getTodo();
                    if(totalString.contains("\n")){
                        String[] result = totalString.split("\n", 2);
                        String ownerString = result[0];
                        String todoString = result[1];
                        todoList.setTag( "PRG_CHANGED" );
                        userSharedWith.setText(ownerString);
                        todoList.setText(todoString);
                        todoList.setTag(null);
                    }else{
                        todoList.setTag( "PRG_CHANGED" );
                        userSharedWith.setText("");
                        todoList.setText(totalString);
                        todoList.setTag(null);
                    }
                });
    }

    @Override
    public void onBackPressed() {finish();}
}
