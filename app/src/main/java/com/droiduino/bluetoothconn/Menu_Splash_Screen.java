package com.droiduino.bluetoothconn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu_Splash_Screen extends AppCompatActivity {
    ImageView Icon;
    TextView AppName;
    Animation uptodown, downtoup,Fadein,FadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_splash_screen);
        Icon=(ImageView) findViewById(R.id.icon);
        AppName=(TextView) findViewById(R.id.appname);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        Fadein = AnimationUtils.loadAnimation(this, R.anim.to_left);
        FadeOut= AnimationUtils.loadAnimation(this, R.anim.to_right);
        Icon.setAnimation(uptodown);
        AppName.setAnimation(downtoup);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goto_page_connect();
            }
        }, 5000);
    }

    private void goto_page_connect() {
        Intent intent=new Intent(Menu_Splash_Screen.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}