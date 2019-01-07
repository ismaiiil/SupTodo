package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;

public class ToDoListActivity extends AppCompatActivity {

//    private Button btnLogout;
//    private Button btnAddToDoList;
//    private Button btnEditToDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        this.setTheme(R.style.AppTheme_NoActionBar);

        String[] todo = {"machin1 \n machin0.5", "machin2", "machin3", "machin4", "machin5", "machin6", "machin7", "machin8", "machin9", "machin10"};
        ListAdapter multiListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todo);
        ListView multiListView = (ListView)findViewById(R.id.multiListView);
        multiListView.setAdapter(multiListViewAdapter);


        multiListView.setOnItemClickListener((parent, view, position, id) -> {
            openEditActivity();
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Your todo lists:");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            openAddActivity();
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
            exitApplication();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUIElements(){

//        btnLogout = (Button)findViewById(R.id.logoutBtn);
//        btnAddToDoList = (Button)findViewById(R.id.addBtn);
//        btnEditToDoList = (Button)findViewById(R.id.editBtn);
    }

    public void exitApplication(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void openAddActivity(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void openEditActivity(){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
