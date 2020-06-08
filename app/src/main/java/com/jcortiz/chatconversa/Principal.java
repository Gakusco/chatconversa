package com.jcortiz.chatconversa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegistro;

    private TextInputLayout layoutUserLogin;
    private TextInputLayout layoutContraLogin;

    private TextInputEditText inputUserLogin;
    private TextInputEditText inputContraLogin;

    private static String idunica = null;
    private static final String PREF_KEY = "ID_UNICA";

    private SharedPreferences preferencias;
    private SharedPreferences.Editor edit;

    private static WebService servicio;

    private final Pattern regexPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflarVariables();
        uuid(this);
        manejoErroresInput();
        clicEnUnBoton();

    }

    private void manejoErroresInput() {
        inputUserLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if((editable.length()>0 && editable.length() < 4) || editable.length() > layoutUserLogin.getCounterMaxLength()){
                    layoutUserLogin.setError("Mínimo 4 caracteres, máximo 8");
                }else{
                    layoutUserLogin.setError(null);
                }
            }
        });

        inputContraLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!regexPassword.matcher(inputContraLogin.getEditableText().toString()).matches() && editable.length()>0) {
                    layoutContraLogin.setError("Minimo 1 mayuscula, 1 minuscula, 1 numero, 6 carectares, Maximo 12 caracteres");
                }else{
                    layoutContraLogin.setError(null);
                }
            }
        });
    }

    private void clicEnUnBoton() {
        btnLogin.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }

    private void inflarVariables() {
        btnRegistro = findViewById(R.id.btnIrAlRegistro);
        btnLogin = findViewById(R.id.btnIniciarSesion);

        layoutUserLogin = findViewById(R.id.layoutUserLogin);
        layoutContraLogin = findViewById(R.id.layoutPasswordLogin);

        inputUserLogin = findViewById(R.id.inputUserLogin);
        inputContraLogin = findViewById(R.id.inputContraLogin);

        preferencias = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIniciarSesion:
                Call<RespuestaLoginWS> resp = servicio.login(inputUserLogin.getText().toString(),inputContraLogin.toString(),preferencias.getString(PREF_KEY,"errorDeDeviceId"));
                resp.enqueue(new Callback<RespuestaLoginWS>() {
                    @Override
                    public void onResponse(Call<RespuestaLoginWS> call, Response<RespuestaLoginWS> response) {

                    }

                    @Override
                    public void onFailure(Call<RespuestaLoginWS> call, Throwable t) {

                    }
                });
                break;
            case R.id.btnIrAlRegistro:
                Intent i = new Intent(this,Registro.class);
                startActivity(i);
                break;
        }
    }

    private void inyecionDependenciaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(WebService.class);
    }

    public synchronized static String uuid(Context context){
        if(idunica == null){
            SharedPreferences preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
            idunica = preferences.getString(PREF_KEY,null);

            if(idunica == null){
                idunica = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREF_KEY, idunica);
                editor.commit();
            }
        }
        return idunica;
    }
}
