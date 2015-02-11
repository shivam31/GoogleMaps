package com.shivamdev.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by shivam.chopra on 12-02-2015.
 */
public class SplashScreen extends Activity {
    private static int SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Executed after timer is finished (Opens MainActivity)
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                // Kills this Activity
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }

}
