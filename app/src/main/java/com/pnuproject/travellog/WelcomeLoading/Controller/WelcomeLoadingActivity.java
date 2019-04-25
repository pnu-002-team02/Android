package com.pnuproject.travellog.WelcomeLoading.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pnuproject.travellog.Main.MainActivity.Controller.MainActivity;
import com.pnuproject.travellog.R;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeLoadingActivity extends AppCompatActivity {
    final long WAIT_TIME_MILLISECOND = 2000; // 2 sec

    private TimerTask timerTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_loading);

        final Intent intent = new Intent(this, MainActivity.class);
        timerTask = new TimerTask() {
            @Override public void run() {
                startActivity(intent);
                finish();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, WAIT_TIME_MILLISECOND);

    }
}
