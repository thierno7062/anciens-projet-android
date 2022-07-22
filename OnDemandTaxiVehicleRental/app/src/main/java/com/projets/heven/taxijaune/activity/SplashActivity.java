package com.projets.heven.taxijaune.activity;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.settings.PrefManager;

public class SplashActivity extends AppCompatActivity {
    private PrefManager prefManager;
    private static int SPLASH_TIME_OUT = 1000;
    private Handler handler = new Handler();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;

        // making notification bar transparent
        changeStatusBarColor();

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
        }else{
            timeHandler();
        }
    }

    private void timeHandler(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                launchHomeScreen();
            }
        },SPLASH_TIME_OUT);
    }

    private void launchHomeScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SplashActivity.this, Connexion.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
