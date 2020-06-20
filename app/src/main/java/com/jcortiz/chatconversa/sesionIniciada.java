package com.jcortiz.chatconversa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.jcortiz.chatconversa.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.respuestasWS.OkRequestWS;

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

    private AlertDialog.Builder builder;

    private TextView textoNombre;
    private TextView textoApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_iniciada);
        inyecionDependenciaRetrofit();
        inflarComponentes();
        barraDeAccion();

        Log.d("Retrofit",token);
        Log.d("Retrofit",idUser);
        Log.d("Retrofit",name);

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

        builder = new AlertDialog.Builder(this);
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
                builder.setMessage(R.string.mensaje)
                        .setTitle(R.string.confirmar)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Call<OkRequestWS> resp = servicio.logout(token,idUser,username);

                                resp.enqueue(new Callback<OkRequestWS>() {
                                    @Override
                                    public void onResponse(Call<OkRequestWS> call, Response<OkRequestWS> response) {
                                        if(response != null && response.body() != null){
                                            SharedPreferences pref = getSharedPreferences(Principal.PREF_KEY,0);
                                            pref.edit().clear().commit();
                                            Intent i = new Intent(sesionIniciada.this,splashLogout.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            if(!response.isSuccessful()) {
                                                Gson gson = new Gson();
                                                BadRequest errorCerrar = gson.fromJson(response.errorBody().charStream(), BadRequest.class);
                                                if(errorCerrar.getErrors() != null){
                                                    if(errorCerrar.getErrors().getUserId() != null){
                                                        Toast.makeText(sesionIniciada.this,""+errorCerrar.getErrors().getUserId(),Toast.LENGTH_LONG).show();
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

                                    @Override
                                    public void onFailure(Call<OkRequestWS> call, Throwable t) {

                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

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
