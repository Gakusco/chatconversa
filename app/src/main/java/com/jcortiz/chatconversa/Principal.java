package com.jcortiz.chatconversa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflarVariables();
        clicEnUnBoton();

    }

    private void clicEnUnBoton() {
        btnRegistro.setOnClickListener(this);
    }

    private void inflarVariables() {
        btnRegistro = findViewById(R.id.btnIrAlRegistro);
    }


    @Override
    public void onClick(View view) {
        Intent i = new Intent(this,Registro.class);
        startActivity(i);
    }
}
