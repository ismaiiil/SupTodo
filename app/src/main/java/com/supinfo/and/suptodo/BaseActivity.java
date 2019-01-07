package com.supinfo.and.suptodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supinfo.and.suptodo.API.APICallHandler;
import com.supinfo.and.suptodo.API.ApiUtil;
import com.supinfo.and.suptodo.API.UserService;
import com.supinfo.and.suptodo.SQLITE.DeleteAllCompletionHandler;
import com.supinfo.and.suptodo.SQLITE.GetUserCompletionHandler;
import com.supinfo.and.suptodo.SQLITE.SQLITEAsyncTasks;
import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.SQLITE.UserDao;
import com.supinfo.and.suptodo.SQLITE.UserRoomDatabase;
import com.supinfo.and.suptodo.model.MessageResponse;
import com.supinfo.and.suptodo.model.StateResponse;
import com.supinfo.and.suptodo.model.TodoResponse;
import com.supinfo.and.suptodo.model.UserResponse;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    APICallHandler APIInstance = APICallHandler.getINSTANCE();
    UserDao userDao;

    public String LOGGED_USER_KEY = "LOGGED_USER_KEY";
    public String PASSED_TODO = "PASSED_TODO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        UserRoomDatabase db = UserRoomDatabase.getDatabase(this.getApplication());
        userDao = db.userDao();
    }

    public void showProgressDialog(String withText) {
        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(withText);
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog diaBox = askOption();
        diaBox.show();
    }

    private AlertDialog askOption()
    {
        return new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton("Yes", (dialog, whichButton) -> finishAffinity())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();

    }

    public void startRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void startLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    User getCachedUser(GetUserCompletionHandler getUserCompletionHandler) throws ExecutionException, InterruptedException{
        return new SQLITEAsyncTasks.GetCachedUserAsyncTask(userDao,getUserCompletionHandler).execute().get();
    }

    public void insert (User user) {
        new SQLITEAsyncTasks.InsertAsyncTask(userDao).execute(user);
    }

    public void deleteAll(DeleteAllCompletionHandler deleteAllCompletionHandler){ new SQLITEAsyncTasks.DeleteAllAsyncTask(userDao,deleteAllCompletionHandler).execute(); }







}
