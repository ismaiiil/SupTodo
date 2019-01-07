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
    UserService userService = ApiUtil.getUserService();
    APICallHandler APIInstance = APICallHandler.getINSTANCE();
    UserDao userDao;
    User cachedUser;

    public String LOGGED_USER_KEY = "LOGGED_USER_KEY";

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
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton("Yes", (dialog, whichButton) -> finishAffinity())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();
        return myQuittingDialogBox;

    }
    public void startRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    User getCachedUser() throws ExecutionException, InterruptedException{
        return new GetCachedUserAsyncTask(userDao,this).execute().get();
    }

    private void insert (User user) {
        new InsertAsyncTask(userDao).execute(user);
    }

    public void deleteAllAndInsert(User user){ new DeleteAndInsertAsyncTask(userDao,this,user).execute(); }

    public void deleteAll(){ new DeleteAllAsyncTask(userDao).execute(); }

    private static class DeleteAndInsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;
        private User user;
        WeakReference<BaseActivity> baseActivity;

        DeleteAndInsertAsyncTask(UserDao dao, BaseActivity baseActivity , User user) {
            mAsyncTaskDao = dao;
            this.baseActivity = new WeakReference<BaseActivity>(baseActivity);
            this.user = user;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            baseActivity.get().insert(user);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        InsertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao mAsyncTaskDao;

        DeleteAllAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }


    }

    private static class GetCachedUserAsyncTask extends android.os.AsyncTask<Void, Void, User> {

        private UserDao mAsyncTaskDao;
        WeakReference<BaseActivity> baseActivity;

        GetCachedUserAsyncTask(UserDao dao, BaseActivity baseActivity) {
            mAsyncTaskDao = dao;
            this.baseActivity = new WeakReference<BaseActivity>(baseActivity);
        }

        @Override
        protected User doInBackground(Void... voids) {
            return mAsyncTaskDao.getCachedUser();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if(user != null){
                baseActivity.get().APIInstance.loginUser(baseActivity.get(),user,null);
            }else{
                baseActivity.get().startRegisterActivity();
            }

        }
    }



}
