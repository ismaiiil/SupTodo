package com.supinfo.and.suptodo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.supinfo.and.suptodo.SQLITE.User;
import com.supinfo.and.suptodo.SQLITE.UserDao;
import com.supinfo.and.suptodo.SQLITE.UserRoomDatabase;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setTitle(R.string.title_activity_launch);
        try {
            getCachedUser();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

