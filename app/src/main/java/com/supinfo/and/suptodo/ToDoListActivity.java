package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private UserResponse loggedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        loggedUser = (UserResponse) getIntent().getSerializableExtra(LOGGED_USER_KEY);
        setTitle("Your Todo Lists: " + loggedUser.getLastname() + " " + loggedUser.getFirstname() );

        refreshListViewFromAPI();

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
        ListView multiListView = (ListView)findViewById(R.id.multiListView);


        multiListView.setOnItemClickListener((parent, view, position, id) -> {
            openEditActivity((TodoResponse) parent.getItemAtPosition(position));
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Your todo lists:");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            System.out.println("logout pressed");
            logoutAndCloseActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logoutAndCloseActivity(){
        APIInstance.logoutUser(this,this);
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


    // do this to windows you want back button to behave normally
//    @Override
//    public void onBackPressed() {finish();}
}
