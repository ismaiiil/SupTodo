package com.supinfo.and.suptodo;

import android.os.Handler;
import android.os.Bundle;
import android.text.InputType;
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
    User loggedUser;
    Boolean isEditing;
    TodoResponse IntentToDoResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        todolist = (EditText) findViewById(R.id.listDisplayEdit);
        userSharedwith = (TextView) findViewById(R.id.sharedWithEdit) ;

        IntentToDoResponse = (TodoResponse) getIntent().getSerializableExtra(PASSED_TODO);
        loggedUser = (User) getIntent().getSerializableExtra(LOGGED_USER_KEY);
        String totalString = IntentToDoResponse.getTodo();

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

        if(todolist.getText().toString().isEmpty()){
            todolist.append(" = ");
        }

        Button updatebtn = (Button) findViewById(R.id.updateBtnEdit);
        updatebtn.setOnClickListener(v -> {
            APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                    ,Integer.parseInt(IntentToDoResponse.getId()), userSharedwith.getText() + "\n" + todolist.getText().toString() );
            isEditing = false;
        });

        Button cancelbtn = (Button) findViewById(R.id.cancelBtnEdit);
        cancelbtn.setOnClickListener(v -> {
            finish();
        });

        todolist.setImeOptions(EditorInfo.IME_ACTION_DONE);
        todolist.setRawInputType(InputType.TYPE_CLASS_TEXT);
        todolist.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                int start = Math.max(todolist.getSelectionStart(), 0);
                int end = Math.max(todolist.getSelectionEnd(), 0);
                todolist.getText().replace(Math.min(start, end), Math.max(start, end),
                        "\n = ", 0, "\n = ".length());
                APIInstance.updateTodoByID(this,loggedUser.getUsername(),loggedUser.getPassword()
                        ,Integer.parseInt(IntentToDoResponse.getId()), userSharedwith.getText() + "\n" + todolist.getText().toString() );
                isEditing = false;
            }
            System.out.println(actionId);
            return false;
        });

        // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                updateView();
                handler.postDelayed(this, 30000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);

    }

    protected void updateView(){
        APIInstance.readToDoByID(this, loggedUser.getUsername(), loggedUser.getPassword()
                , Integer.parseInt(IntentToDoResponse.getId()), todoResponse -> {
                    String totalString = todoResponse.getTodo();
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
                });
    }

    @Override
    public void onBackPressed() {finish();}
}
