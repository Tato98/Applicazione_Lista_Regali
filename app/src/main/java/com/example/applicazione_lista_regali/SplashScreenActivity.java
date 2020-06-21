package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

//Classe che gestisce la visualizzazione dello splash screen
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 4000;    //durata splash screen

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        //______________________________________________ Splash Screen ____________________________________________________
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashScreen = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(splashScreen);
                finish();
            }
        },SPLASH_TIME_OUT);
        //__________________________________________________________________________________________________________________

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.gifty);
    }
}
