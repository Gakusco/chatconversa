package com.jcortiz.chatconversa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        barraDeAccion();


    }

    private void barraDeAccion() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_chat_conversa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
