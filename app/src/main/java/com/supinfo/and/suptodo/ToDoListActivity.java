package com.supinfo.and.suptodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;

public class ToDoListActivity extends BaseActivity {

    private Button btnLogout;
    private Button btnAddToDoList;
    private Button btnEditToDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setTitle(R.string.title_activity_todolist);

        getUIElements();

        btnLogout.setOnClickListener(v ->
                exitApplication()

        );

        btnAddToDoList.setOnClickListener(v ->
                openAddActivity()
        );

        btnEditToDoList.setOnClickListener(v ->
                openEditActivity()
        );

        String[] todo = {"machin1 \n machin0.5", "machin2", "machin3", "machin4", "machin5", "machin6", "machin7", "machin8", "machin9", "machin10"};
        ListAdapter multiListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todo);
        ListView multiListView = (ListView)findViewById(R.id.multiListView);
        multiListView.setAdapter(multiListViewAdapter);
    }

    private void getUIElements(){

        btnLogout = (Button)findViewById(R.id.logoutBtn);
        btnAddToDoList = (Button)findViewById(R.id.addBtn);
        btnEditToDoList = (Button)findViewById(R.id.editBtn);
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


// do this to windows you want back button to behave normally
//    @Override
//    public void onBackPressed() {finish();}
}
