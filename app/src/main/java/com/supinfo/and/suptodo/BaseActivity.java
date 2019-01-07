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
                baseActivity.get().loginUser(user,null);
            }else{
                baseActivity.get().startRegisterActivity();
            }

        }
    }

    protected void loginUser(User user,Activity caller){
        Call<JsonObject> call = userService.login(user.getUsername(),user.getPassword());
        showProgressDialog("Logging in");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    Intent intent = new Intent(BaseActivity.this, ToDoListActivity.class);
                    intent.putExtra(LOGGED_USER_KEY,userResponse);
                    //add logged user to SQLITE
                    deleteAllAndInsert(new User(userResponse.getUsername(),userResponse.getPassword()));
                    startActivity(intent);
                    hideProgressDialog();
                    if(caller != null){
                        caller.finish();
                    }
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    hideProgressDialog();
                    String message = "invalid credentials: " + messageResponse.getMessage();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }else{
                    //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    String message = "invalid info provided";
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                hideProgressDialog();
                caller.finishAffinity();
            }

        });
    }

    protected void registerUser(String userName,String password,String firstName,
                                String lastName,String email,Activity caller){
        Call<JsonObject> call = userService.register(userName,password,firstName,
                lastName,email);
        System.out.println("calling api from testRegister");
        showProgressDialog("Registering");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testRegister");
                if (response.body().has("id")){
                    UserResponse userResponse = gson.fromJson(response.body(),UserResponse.class);
                    System.out.println("the user has name: " + userResponse.getFirstname() + " " + userResponse.getLastname() + " was registered and logged in");
                    //add registered user to SQLITE
                    deleteAllAndInsert(new User(userResponse.getUsername(),userResponse.getPassword()));
                    Intent intent = new Intent(BaseActivity.this, ToDoListActivity.class);
                    intent.putExtra(LOGGED_USER_KEY,userResponse);
                    startActivity(intent);
                    hideProgressDialog();
                    caller.finish();
                } else if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    String message = "You were not registered since: " + messageResponse.getMessage();
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }else{
                    //StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    String message = "invalid info provided";
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }

    protected void logoutUser(Activity caller){
        showProgressDialog("Logging Out");
        Call<StateResponse> call = userService.logout();
        System.out.println("calling api from testLogout");
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                hideProgressDialog();
                System.out.println("finished calling API from testLogout");
                startRegisterActivity();
                deleteAll();
                caller.finish();
                String message = "You were sucessfully logged out: " + response.body().isSuccess();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                startRegisterActivity();
                deleteAll();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                caller.finish();
            }
        });
    }

    protected void listFromAPI(String username,String password, MyTodoListCompletionHandler myListener){
        showProgressDialog("Refreshing");
        Call<List<TodoResponse>> call = userService.list(username,password);
        System.out.println("calling api from testList");
        call.enqueue(new Callback<List<TodoResponse>>() {
            @Override
            public void onResponse(Call<List<TodoResponse>> call, Response<List<TodoResponse>> response) {
                myListener.onFinished(response.body());
                hideProgressDialog();

            }
            @Override
            public void onFailure(Call<List<TodoResponse>> call, Throwable t) {
                System.out.println(t);
                String message = "Something went wrong when trying to connect to the server";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });

    }

    public interface MyTodoListCompletionHandler {
        void onFinished(List<TodoResponse> todoResponses);
    }


    protected void shareWithFriend(String username,String password,String userFriend, ShareCompletionHandler shareCH) {
        showProgressDialog("Adding your friend");
        Call<JsonObject> call = userService.share(username,password,userFriend);
        System.out.println("calling api from testShare");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testShare");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not shared: " + messageResponse.getMessage());
                    shareCH.onFinished(false);
                    hideProgressDialog();
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was shared: " +stateResponse.isSuccess());
                        shareCH.onFinished(true);
                        hideProgressDialog();
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                        shareCH.onFinished(false);
                        hideProgressDialog();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
                hideProgressDialog();
            }

        });
    }

    public interface ShareCompletionHandler{
        void onFinished(Boolean wasShared);
    }


    protected void updateTodoByID(String username,String password, int id, String todo) {
        showProgressDialog("Updating your todo!");
        Call<JsonObject> call = userService.update(username,password,id,todo);
        System.out.println("calling api from testUpdate");
        call.enqueue(new Callback<JsonObject>() {
            Gson gson = new Gson();
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("finished calling API from testUpdate");
                if (response.body().has("message")){
                    MessageResponse messageResponse = gson.fromJson(response.body(),MessageResponse.class);
                    System.out.println("the todo was not updated: " + messageResponse.getMessage());
                    hideProgressDialog();
                }else{
                    StateResponse stateResponse = gson.fromJson(response.body(),StateResponse.class);
                    if (stateResponse.isSuccess()) {
                        System.out.println("the todo was updated: " +stateResponse.isSuccess());
                        hideProgressDialog();
                    }else{
                        System.out.println("server rejected this action " +stateResponse.isSuccess());
                        hideProgressDialog();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t);
                System.out.println("Something went wrong when trying to connect to the server");
                hideProgressDialog();
            }

        });
    }

}
