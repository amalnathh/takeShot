package com.deftech.takeshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                checkData();
            }
        }, 800);
    }

    private void checkData() {
        if (!preferenceFileExists("userDetailsArraySp")) {
            Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, TabbedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean preferenceFileExists(String filename) {
        File f = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/" + filename + ".xml");
        return f.exists();
    }
}