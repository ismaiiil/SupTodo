package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.Bundle;
import com.supinfo.and.suptodo.API.CompletionHandlers;
import com.supinfo.and.suptodo.SQLITE.User;
import java.util.concurrent.ExecutionException;
public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setTitle(R.string.title_activity_launch);
        try {
            getCachedUser(user -> {
                if(user != null){
                    APIInstance.loginUser(this, user, new CompletionHandlers.MyLoginCompletionHandler() {
                        @Override
                        public void onSucceeded(Boolean wasAuthenticated) {
                            loginNotAuth(wasAuthenticated);
                        }

                        @Override
                        public void onFailed() {
                            loginFailed(user);
                        }
                    });
                }else{
                    startRegisterActivity();
                }
            });

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loginNotAuth(boolean wasAuthenticated){
        if(!wasAuthenticated){
            startRegisterActivity();
            APIInstance.makeToast(this,"Your credentials are no more valid!");
        }
    }

    private void loginFailed(User user){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LOGGED_USER_KEY,new User(user.getUsername(),user.getPassword()));
        startActivity(intent);
        APIInstance.makeToast(this,"Server did not respond to Login");
    }

}

