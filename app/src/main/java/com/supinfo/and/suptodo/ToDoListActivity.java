package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoListActivity extends BaseActivity {

    private Button btnLogout;
    private Button btnAddToDoList;
    private Button btnEditToDoList;
    private UserResponse loggedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        loggedUser = (UserResponse) getIntent().getSerializableExtra(LOGGED_USER_KEY);
        setTitle("Your Todo Lists: " + loggedUser.getLastname() + " " + loggedUser.getFirstname() );

        getUIElements();

        refreshListViewFromAPI();

        btnLogout.setOnClickListener(v ->
                logoutAndCloseActivity()

        );

        btnAddToDoList.setOnClickListener(v ->
                openAddActivity()
        );

        btnEditToDoList.setOnClickListener(v ->
                openEditActivity()
        );
        ListView multiListView = (ListView)findViewById(R.id.multiListView);


        multiListView.setOnItemClickListener((parent, view, position, id) -> {
            view.setSelected(true);
            TodoResponse mTodoResponse = (TodoResponse) parent.getItemAtPosition(position);
            Toast.makeText(getBaseContext(),"id selected is " + mTodoResponse.getId(),Toast.LENGTH_LONG).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListViewFromAPI();
    }

    private void refreshListViewFromAPI() {
        APIInstance.listFromAPI(this,loggedUser.getUsername(), loggedUser.getPassword(), todoResponses -> {
            ListView multiListView = (ListView) findViewById(R.id.multiListView);
            ToDoItemAdapter multiListViewAdapter = new ToDoItemAdapter(this,todoResponses);
            multiListView.setAdapter(multiListViewAdapter);
        });
    }

    private void getUIElements(){

        btnLogout = (Button)findViewById(R.id.logoutBtn);
        btnAddToDoList = (Button)findViewById(R.id.addBtn);
        btnEditToDoList = (Button)findViewById(R.id.editBtn);
    }

    public void logoutAndCloseActivity(){
        APIInstance.logoutUser(this,this);
    }

    public void openAddActivity(){
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra(LOGGED_USER_KEY,new User(loggedUser.getUsername(),loggedUser.getPassword()));
        startActivity(intent);
    }

    public void openEditActivity(){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }





    // do this to windows you want back button to behave normally
//    @Override
//    public void onBackPressed() {finish();}
}
