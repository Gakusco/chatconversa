package com.jcortiz.chatconversa.splashs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.views.HomeView;

public class SplashLogin extends AppCompatActivity {

    private final int DURACION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashLogin.this, HomeView.class);
                startActivity(i);
                finish();
            };
        },DURACION);
    }
}
