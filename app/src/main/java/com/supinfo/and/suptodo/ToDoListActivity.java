package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;

public class ToDoListActivity extends BaseActivity {

    private UserResponse loggedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        loggedUser = (UserResponse) getIntent().getSerializableExtra(LOGGED_USER_KEY);
        refreshListViewFromAPI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListViewFromAPI();
    }

    private void refreshListViewFromAPI() {
        APIInstance.listFromAPI(this,loggedUser.getUsername(), loggedUser.getPassword(), todoResponses -> {
            ListView multiListView = findViewById(R.id.multiListView);
            ToDoItemAdapter multiListViewAdapter = new ToDoItemAdapter(this,todoResponses);
            multiListView.setAdapter(multiListViewAdapter);
        });
        ListView multiListView = findViewById(R.id.multiListView);
        multiListView.setOnItemClickListener((parent, view, position, id) -> openEditActivity((TodoResponse) parent.getItemAtPosition(position)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("ID: " + loggedUser.getId() + " Username: " + loggedUser.getUsername() );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(multiListView.getAdapter().getCount()==50){
               fab.setActivated(false);
            }else{
                openAddActivity();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            System.out.println("logout pressed");
            logoutAndCloseActivity();
            return true;
        }else if (id == R.id.action_refresh){
            System.out.println("refresh pressed");
            refreshListViewFromAPI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logoutAndCloseActivity(){
        AlertDialog alertDialog = logoutDialog();
        alertDialog.show();
    }

    public void openAddActivity(){
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra(LOGGED_USER_KEY,new User(loggedUser.getUsername(),loggedUser.getPassword()));
        startActivity(intent);
    }

    public void openEditActivity(TodoResponse todoResponse){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(LOGGED_USER_KEY,new User(loggedUser.getUsername(),loggedUser.getPassword()));
        intent.putExtra(PASSED_TODO,todoResponse);
        startActivity(intent);
    }

    protected AlertDialog logoutDialog()
    {
        return new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to Logout?")
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("Yes", (dialog, whichButton) -> APIInstance.logoutUser(this,this))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();

    }


}
