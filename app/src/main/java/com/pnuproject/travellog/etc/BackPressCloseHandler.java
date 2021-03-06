package com.pnuproject.travellog.etc;

/**
 * Created by s0woo on 2019-04-02.
 */

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        long currentTimeMillis =  System.currentTimeMillis();
        if (currentTimeMillis > backKeyPressedTime + 2000) {
            backKeyPressedTime = currentTimeMillis;
            showGuide();
        } else {
            activity.finish();
            toast.cancel();
        }

    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}