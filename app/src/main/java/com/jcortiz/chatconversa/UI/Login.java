package com.jcortiz.chatconversa.UI;

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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.jcortiz.chatconversa.utilidades.Constantes;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.OkRequestWS;
import com.jcortiz.chatconversa.splashes.splashLogin;

import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegistro;

    private TextInputLayout layoutUserLogin;
    private TextInputLayout layoutContraLogin;

    private TextInputEditText inputUserLogin;
    private TextInputEditText inputContraLogin;

    private TextView mostrarMessageError;

    private static String idunica = null;
    public static final String PREF_KEY = "ID_UNICA";

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
        revisarPreferencias();
        manejoErroresInput();
        clicEnUnBoton();

    }

    private void revisarPreferencias() {
        String userCarga = preferencias.getString("user","");
        String passCarga = preferencias.getString("password","");
        if(userCarga != "" && passCarga != ""){
            Intent i = new Intent(Login.this, Principal.class);
            startActivity(i);
            finish();
        }
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
                    layoutUserLogin.setError("Mínimo 4 caracteres, máximo 8.");
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
                    layoutContraLogin.setError("Mínimo 1 mayúscula, 1 minúscula, 1 número, 6 caracteres, Máximo 12 caracteres.");
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

        mostrarMessageError = findViewById(R.id.mostrarMessageErrorLogin);

        preferencias = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        edit = preferencias.edit();

        servicio = WSClient.getInstance().getWebService();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIniciarSesion:
                final Call<OkRequestWS> resp = servicio.login(inputUserLogin.getText().toString(),inputContraLogin.getText().toString(),preferencias.getString(PREF_KEY,"errorDeDeviceId"));
                resp.enqueue(new Callback<OkRequestWS>() {
                    @Override
                    public void onResponse(Call<OkRequestWS> call, Response<OkRequestWS> response) {
                        if(response != null && response.body() != null){
                            Log.d("Retrofit",response.body().getUser().getId().toString());
                            mostrarMessageError.setVisibility(View.INVISIBLE);
                            guardarDatosDeUsuario(response.body());

                            Intent i = new Intent(Login.this, splashLogin.class);
                            startActivity(i);
                            finish();

                        }else if(!response.isSuccessful()){
                            Gson gson = new Gson();
                            BadRequest mensajeDeError = gson.fromJson(response.errorBody().charStream(),BadRequest.class);

                            respuestaDeError(mensajeDeError);

                        }
                    }

                    @Override
                    public void onFailure(Call<OkRequestWS> call, Throwable t) {

                    }
                });
                break;
            case R.id.btnIrAlRegistro:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;
        }
    }

    private void respuestaDeError(BadRequest mensajeDeError) {
        if(mensajeDeError.getMessage() != null){
            mostrarMessageError.setVisibility(View.VISIBLE);
            mostrarMessageError.setText(mensajeDeError.getMessage());
        }
        if(mensajeDeError.getErrors() != null){
            if(mensajeDeError.getErrors().getUsername() != null){
                String userError = mensajeDeError.getErrors().getUsername().toString();
                layoutUserLogin.setError(userError.substring(1,userError.length()-1));
            }
            if(mensajeDeError.getErrors().getPassword() != null){
                String passwordError = mensajeDeError.getErrors().getPassword().toString();
                layoutContraLogin.setError(passwordError.substring(1,passwordError.length()-1));
            }
        }
    }

    private void guardarDatosDeUsuario(OkRequestWS body) {
        edit.putString(Constantes.USER,inputUserLogin.getText().toString());
        edit.putString(Constantes.PASSWORD,inputContraLogin.getText().toString());
        edit.putString(Constantes.USER_ID,body.getUser().getId().toString());
        edit.putString(Constantes.TOKEN,body.getToken());
        edit.putString(Constantes.NAME,body.getUser().getName());
        edit.putString(Constantes.LAST_NAME,body.getUser().getLastName());
        edit.putString(Constantes.RUN,body.getUser().getRun());
        edit.putString(Constantes.EMAIL,body.getUser().getEmail());
        edit.putString(Constantes.IMAGE,body.getUser().getImage());
        edit.putString(Constantes.THUMBNAIL,body.getUser().getThumbnail());
        edit.commit();
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
