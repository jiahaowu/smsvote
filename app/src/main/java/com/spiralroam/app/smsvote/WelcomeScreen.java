package com.spiralroam.app.smsvote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class WelcomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_screen);

        final Intent intent = new Intent(this, SMSVoteActivity.class);
        // set delay time to 2 seconds
        long delay = 2000;
        // control the delay time associated with a thread's
        // MessageQueue,delay's unit is ms

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // execute the task
                WelcomeScreen.this.startActivity(intent);
                finish();
            }
        }, delay);
    }
}
