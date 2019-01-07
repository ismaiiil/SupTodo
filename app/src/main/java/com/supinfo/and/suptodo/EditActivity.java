package com.supinfo.and.suptodo;

import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.TodoResponse;

public class EditActivity extends BaseActivity {

    EditText todolist;
    TextView userSharedwith;
    TextView editTextInfo;
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

        todolist = (EditText) findViewById(R.id.listDisplayEdit);
        userSharedwith = (TextView) findViewById(R.id.sharedWithEdit) ;
        editTextInfo = (TextView) findViewById(R.id.textInfoEdit);
        intentToDoResponse = (TodoResponse) getIntent().getSerializableExtra(PASSED_TODO);
        loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);

        setFields();

        if(intentToDoResponse.getUserinvited() != null){
            automateUpdateView();
        }else{
            editTextInfo.setText("Your Private Todo List: ");
        }



        Button updatebtn = (Button) findViewById(R.id.updateBtnEdit);
        updatebtn.setOnClickListener(v -> {
            APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                    ,Integer.parseInt(intentToDoResponse.getId()), userSharedwith.getText() + "\n" + todolist.getText().toString() );
            isEditing = false;
        });

        Button cancelbtn = (Button) findViewById(R.id.cancelBtnEdit);
        cancelbtn.setOnClickListener(v -> {
            finish();
        });


        setToDolistActionEvent();


    }

    private void setToDolistActionEvent(){
        todolist.setImeOptions(EditorInfo.IME_ACTION_DONE);
        todolist.setRawInputType(InputType.TYPE_CLASS_TEXT);
        todolist.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                int start = Math.max(todolist.getSelectionStart(), 0);
                int end = Math.max(todolist.getSelectionEnd(), 0);
                todolist.getText().replace(Math.min(start, end), Math.max(start, end),
                        "\n + ", 0, "\n + ".length());
                APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                        ,Integer.parseInt(intentToDoResponse.getId()), userSharedwith.getText() + "\n" + todolist.getText().toString() );
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
            String todosString = result[1];
            userSharedwith.setText(ownerString);
            todolist.setText(todosString);
        }else{
            userSharedwith.setText("");
            todolist.setText(totalString);
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
        System.out.println("on Pause: "+ isPaused);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        System.out.println("on resume: "+ isPaused);
        if(!isEditing){
            updateView();
        }

    }

    private void automateUpdateView(){
        // Create the Handler object (on the main thread by default)
        handler = new Handler();
        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
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
                if( todolist.getTag() == null ) {
                    isEditing = true;
                    // Value changed by user
                }else {
                    isEditing = false;
                    // Value changed by program
                }
            }

        };
        todolist.addTextChangedListener(textWatcher);
    }

    protected void updateView(){
        APIInstance.readToDoByID(this, loggedUser.getUsername(), loggedUser.getPassword()
                , Integer.parseInt(intentToDoResponse.getId()), todoResponse -> {
                    String totalString = todoResponse.getTodo();
                    if(totalString.contains("\n")){
                        String[] result = totalString.split("\n", 2);
                        String ownerString = result[0];
                        String todosString = result[1];
                        todolist.setTag( "PRG_CHANGED" );
                        userSharedwith.setText(ownerString);
                        todolist.setText(todosString);
                        todolist.setTag(null);
                    }else{
                        todolist.setTag( "PRG_CHANGED" );
                        userSharedwith.setText("");
                        todolist.setText(totalString);
                        todolist.setTag(null);
                    }
                });
    }

    @Override
    public void onBackPressed() {finish();}
}
