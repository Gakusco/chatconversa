package com.jcortiz.chatconversa.splashs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jcortiz.chatconversa.views.LoginView;
import com.jcortiz.chatconversa.R;

public class SplashRegisterOk extends AppCompatActivity {

    private final int DURACION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_registro_exitoso);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashRegisterOk.this, LoginView.class);
                startActivity(i);
                finish();
            };
        },DURACION);
    }
}
