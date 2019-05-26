package com.qader.ahmed.androidmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qader.ahmed.androidmovieapp.presentation.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashScreenActivity extends AppCompatActivity {

    private static int splashTimeOut=5000;

    @BindView(R.id.logo)
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ButterKnife.bind(this);

        initDelayed();


    }

    private void initDelayed() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        },splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(myanim);
    }

    private void startApp() {
        Intent intent = new Intent(SplashScreenActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

}