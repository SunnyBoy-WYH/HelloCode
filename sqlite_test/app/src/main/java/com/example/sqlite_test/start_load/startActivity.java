package com.example.sqlite_test.start_load;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sqlite_test.R;

import java.util.Timer;
import java.util.TimerTask;

public class startActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Timer timer =new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(startActivity.this,load.class));
                finish();
            }
        };timer.schedule(timerTask,2000);
    }
}
