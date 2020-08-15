package com.jcortiz.chatconversa.splashes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jcortiz.chatconversa.UI.Principal;
import com.jcortiz.chatconversa.R;

public class splashLogin extends AppCompatActivity {

    private final int DURACION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashLogin.this, Principal.class);
                startActivity(i);
                finish();
            };
        },DURACION);
    }
}
