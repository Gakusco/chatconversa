package com.jcortiz.chatconversa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText name;
    private TextInputEditText lastName;
    private TextInputEditText run;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputEditText tokenEmprise;

    private Button btnRegistrar;

    private static WebService servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        barraDeAccion();
        inflarComponentes();

        inyecionDependenciaRetrofit();
        btnRegistrar.setOnClickListener(this);
    }

    private void inyecionDependenciaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(WebService.class);
    }

    private void inflarComponentes() {
        name = findViewById(R.id.inputNombre);
        lastName = findViewById(R.id.inputApellidos);
        run = findViewById(R.id.inputRun);
        username = findViewById(R.id.inputUsername);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputContra);
        confirmPassword = findViewById(R.id.inputConfirmacionContra);
        tokenEmprise = findViewById(R.id.inputTokenEmpresa);

        btnRegistrar = findViewById(R.id.btnRegistrar);
    }

    private void barraDeAccion() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_chat_conversa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        final Call<RespuestaRegistroWS> resp = servicio.register(
                name.getText().toString(),
                lastName.getText().toString(),
                run.getText().toString(),
                username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                tokenEmprise.getText().toString()
        );

        resp.enqueue(new Callback<RespuestaRegistroWS>() {
            @Override
            public void onResponse(Call<RespuestaRegistroWS> call, Response<RespuestaRegistroWS> response) {
                if(response != null && response.body() != null){
                    RespuestaRegistroWS respuestaWS = response.body();
                    Log.d("Retrofit","Registro exitoso "+response.body());
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        String message = json.getString("message");
                        String name = json.getJSONObject("errors").getString("name");
                        Log.d("Retrofit","Respuesta fallida "+name);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<RespuestaRegistroWS> call, Throwable t) {
                Log.d("Retrofit","Error "+t.getMessage());
            }
        });
    }
}
