package com.jcortiz.chatconversa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private TextInputLayout layoutName;
    private TextInputLayout layoutLastName;
    private TextInputLayout layoutRun;
    private TextInputLayout layoutUsername;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputLayout layoutConfirmPassword;
    private TextInputLayout layoutTokenEmprise;

    private Button btnRegistrar;;

    private TextView textoError;

    private final Pattern regexPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$");
    private final Pattern regextTokenEnterprise = Pattern.compile("^[A-Z\\d]{6,6}$");

    private static WebService servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        barraDeAccion();
        inflarComponentes();
        manejodeDeErroresInputs();



        inyecionDependenciaRetrofit();
        btnRegistrar.setOnClickListener(this);
    }

    private void manejodeDeErroresInputs() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > layoutName.getCounterMaxLength()){
                    layoutName.setError("Ha superado el máximo de caracteres");
                }else{
                    layoutName.setError(null);
                }
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > layoutLastName.getCounterMaxLength()){
                    layoutLastName.setError("Ha superado el máximo de caracteres");
                }else{
                    layoutLastName.setError(null);
                }
            }
        });

        run.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if((editable.length()>0 && editable.length() < 7) || editable.length() > layoutRun.getCounterMaxLength()){
                    layoutRun.setError("Mínimo 7 números, máximo 8");
                }else{
                    layoutRun.setError(null);
                }
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if((editable.length()>0 && editable.length() < 4) || editable.length() > layoutUsername.getCounterMaxLength()){
                    layoutUsername.setError("Mínimo 4 caracteres, máximo 8");
                }else{
                    layoutUsername.setError(null);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!Patterns.EMAIL_ADDRESS.matcher(email.getEditableText().toString().trim()).matches() && editable.length()>0){
                    layoutEmail.setError("Debe tener formato email y no superar los 50 caracteres");
                }else{
                    layoutEmail.setError(null);
                }
            }

        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!regexPassword.matcher(password.getEditableText().toString()).matches() && editable.length()>0) {
                    layoutPassword.setError("Minimo 1 mayuscula, 1 minuscula, 1 numero, 6 carectares, Maximo 12 caracteres");
                }else{
                    layoutPassword.setError(null);
                }
                if(!confirmPassword.getEditableText().toString().equals(password.getText().toString())){
                    layoutConfirmPassword.setError("Las contraseñas son distintas");
                }else{
                    layoutConfirmPassword.setError(null);
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!confirmPassword.getEditableText().toString().equals(password.getText().toString())){
                    layoutConfirmPassword.setError("Las contraseñas son distintas");
                }else{
                    layoutConfirmPassword.setError(null);
                }
            }
        });

        tokenEmprise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!regextTokenEnterprise.matcher(tokenEmprise.getEditableText().toString()).matches()){
                    layoutTokenEmprise.setError("Solo mayusculas y numeros");
                }else{
                    layoutTokenEmprise.setError(null);
                }
            }
        });

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

        layoutName = findViewById(R.id.layoutNombre);
        layoutLastName = findViewById(R.id.layoutApellidos);
        layoutRun = findViewById(R.id.layoutRun);
        layoutUsername = findViewById(R.id.layoutUsername);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);
        layoutTokenEmprise = findViewById(R.id.layoutTokenEmpresa);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        textoError = findViewById(R.id.mensajeDeErrorRegistro);
    }

    private void barraDeAccion() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_chat_conversa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(confirmPassword.getText().toString().isEmpty()){
            layoutConfirmPassword.setError("Campo obligatorio");
            return;
        }
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
                    Toast.makeText(Registro.this,"Registro exitoso",Toast.LENGTH_LONG).show();
                    textoError.setVisibility(View.GONE);
                }else if(!response.isSuccessful()) {
                    Gson gson = new Gson();
                    mensajeErrorRegistro mensajeDeError = gson.fromJson(response.errorBody().charStream(),mensajeErrorRegistro.class);
                    if(mensajeDeError.getMessage() != null){
                        textoError.setVisibility(View.VISIBLE);
                        textoError.setText(mensajeDeError.getMessage());
                    }
                    if(mensajeDeError.getErrors() != null){
                        if(mensajeDeError.getErrors().getName() != null){
                            String nameError = mensajeDeError.getErrors().getName().toString();
                            layoutName.setError(nameError.substring(1,nameError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getLastname() != null){
                            String lastNameError = mensajeDeError.getErrors().getLastname().toString();
                            layoutLastName.setError(lastNameError.substring(1,lastNameError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getRun() != null){
                            String runError = mensajeDeError.getErrors().getRun().toString();
                            layoutLastName.setError(runError.substring(1,runError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getUsername() != null){
                            String userError = mensajeDeError.getErrors().getUsername().toString();
                            layoutUsername.setError(userError.substring(1,userError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getEmail() != null){
                            String emailError = mensajeDeError.getErrors().getEmail().toString();
                            layoutEmail.setError(emailError.substring(1,emailError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getPassword() != null){
                            String passwordError = mensajeDeError.getErrors().getPassword().toString();
                            layoutPassword.setError(passwordError.substring(1,passwordError.length()-1));
                        }

                        if(mensajeDeError.getErrors().getTokenEnterprise() != null){
                            String tokenError = mensajeDeError.getErrors().getTokenEnterprise().toString();
                            layoutTokenEmprise.setError(tokenError.substring(1,tokenError.length()-1));
                        }
                    }
                    /*try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        String message = json.getString("message");
                        JSONObject obtenerErrores = json.getJSONObject("errors");
                        String tokenError = "";
                        String correoError = "";
                        String usernameError = "";
                        String nombreError = "";
                        String apellidoError = "";
                        String passwordError = "";
                        String runError = "";
                        String obtenerString = "";
                        if (obtenerErrores.has("name")) {
                            obtenerString = obtenerErrores.getString("name");
                            nombreError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutName.setError(nombreError);
                        }
                        if (obtenerErrores.has("run")) {
                            obtenerString = obtenerErrores.getString("run");
                            runError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutRun.setError(runError);
                        }
                        if (obtenerErrores.has("lastname")) {
                            obtenerString = obtenerErrores.getString("lastname");
                            apellidoError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutLastName.setError(apellidoError);
                        }
                        if (obtenerErrores.has("password")) {
                            obtenerString = obtenerErrores.getString("password");
                            passwordError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutPassword.setError(passwordError);
                        }
                        if (obtenerErrores.has("token_enterprise")) {
                            obtenerString = obtenerErrores.getString("token_enterprise");
                            tokenError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutTokenEmprise.setError(tokenError);
                        }
                        if (obtenerErrores.has("username")) {
                            obtenerString = obtenerErrores.getString("username");
                            usernameError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutUsername.setError(usernameError);
                        }
                        if (obtenerErrores.has("email")) {
                            obtenerString = obtenerErrores.getString("email");
                            correoError = obtenerString.substring(2, obtenerString.length() - 3);
                            layoutEmail.setError(correoError);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }*/

                }
            }

            @Override
            public void onFailure(Call<RespuestaRegistroWS> call, Throwable t) {
                Log.d("Retrofit","Error "+t.getMessage());
            }
        });
    }
}
