package com.jcortiz.chatconversa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcortiz.chatconversa.clasesDeError.errorCerrarSesion;
import com.jcortiz.chatconversa.respuestasWS.RespuestaCerrarSesionWS;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sesionIniciada extends AppCompatActivity {

    private static WebService servicio;
    private SharedPreferences preferencias;

    private String token;
    private String idUser;
    private String name;
    private String lastName;
    private String username;
    private String run;
    private String email;
    private String image;
    private String thumbnail;

    private TextView textoNombre;
    private TextView textoApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_iniciada);
        inyecionDependenciaRetrofit();
        inflarComponentes();
        barraDeAccion();

        textoNombre.setText(name);
        textoApellido.setText(lastName);
    }

    private void inflarComponentes() {
        preferencias = getSharedPreferences(Principal.PREF_KEY,MODE_PRIVATE);
        token = "Bearer "+preferencias.getString("token","errorToken");
        idUser = preferencias.getString("userId","errorId");
        name = preferencias.getString("name","errorName");
        lastName = preferencias.getString("lastName","errorLastname");
        username = preferencias.getString("user","errorUser");
        run = preferencias.getString("run","errorRun");
        email = preferencias.getString("email","errorEmail");
        image = preferencias.getString("image","errorImage");
        thumbnail = preferencias.getString("thumbnail","errorThumbnail");

        textoNombre = findViewById(R.id.nombreUsuario);
        textoApellido = findViewById(R.id.apellidoUsuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_en_toolbar,menu);
        return true;
    }

    private void barraDeAccion() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_chat_conversa);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrarSesionID:
                Log.d("Retrofit",token+" "+username+" "+idUser);
                final Call<RespuestaCerrarSesionWS> resp = servicio.logout(token,idUser,username);

                resp.enqueue(new Callback<RespuestaCerrarSesionWS>() {
                    @Override
                    public void onResponse(Call<RespuestaCerrarSesionWS> call, Response<RespuestaCerrarSesionWS> response) {
                        if(response != null && response.body() != null){
                            SharedPreferences pref = getSharedPreferences(Principal.PREF_KEY,0);
                            pref.edit().clear().commit();
                            Intent i = new Intent(sesionIniciada.this,splashLogout.class);
                            startActivity(i);
                            finish();
                        }else{
                            if(!response.isSuccessful()){
                                if(!response.isSuccessful()) {
                                    Gson gson = new Gson();
                                    errorCerrarSesion errorCerrar = gson.fromJson(response.errorBody().charStream(), errorCerrarSesion.class);
                                    if(errorCerrar.getErrors() != null){
                                        if(errorCerrar.getErrors().getUserId() != null){
                                            Log.d("Retrofit",errorCerrar.getErrors().getUserId().toString());
                                        }
                                        if(errorCerrar.getErrors().getUsername() != null){
                                            Log.d("Retrofit",errorCerrar.getErrors().getUsername().toString());
                                        }
                                    }
                                    Toast.makeText(sesionIniciada.this,errorCerrar.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaCerrarSesionWS> call, Throwable t) {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inyecionDependenciaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(WebService.class);
    }


}
