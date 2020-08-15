package com.jcortiz.chatconversa.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jcortiz.chatconversa.utilidades.Constantes;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.OkRequestWS;
import com.jcortiz.chatconversa.splashes.splashLogout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal extends AppCompatActivity {

    private static WebService servicio;
    private SharedPreferences preferencias;

    private String token;
    private String idUser;
    private String name;
    private String lastName;
    private String username;
    private String run;
    private String email;
    private String thumbnail;
    private String imagenUser;

    //Header
    private View header;

    private TextView textoNombreHeader;
    private TextView textoCorreoHeader;
    private ImageView imagenHeader;

    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        header = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_tomar_foto, R.id.nav_sobre_mi)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        inflarComponentes();
        modificarHeader();
    }

    public void modificarHeader() {
        textoNombreHeader.setText(name+" "+lastName);
        textoCorreoHeader.setText(email);
        Log.d("Retrofit","La imagen es: "+imagenUser);
        if(imagenUser != Constantes.ERROR_IMAGE && imagenUser != null && !imagenUser.isEmpty()) {
            Picasso.get().load(imagenUser).transform(new CropCircleTransformation()).into(imagenHeader);
        }
    }


    public void inflarComponentes() {
        preferencias = getSharedPreferences(Login.PREF_KEY,MODE_PRIVATE);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
        idUser = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        name = preferencias.getString(Constantes.NAME,Constantes.ERROR_NAME);
        lastName = preferencias.getString(Constantes.LAST_NAME,Constantes.ERROR_LAST_NAME);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        run = preferencias.getString(Constantes.RUN,Constantes.ERROR_RUN);
        email = preferencias.getString(Constantes.EMAIL,Constantes.ERROR_EMAIL);
        imagenUser = preferencias.getString(Constantes.IMAGE,Constantes.ERROR_IMAGE);
        thumbnail = preferencias.getString(Constantes.THUMBNAIL,Constantes.ERROR_THUMBNAIL);

        textoCorreoHeader = header.findViewById(R.id.textEmailHeader);
        textoNombreHeader = header.findViewById(R.id.textNombreHeader);
        imagenHeader = header.findViewById(R.id.imagenUsuarioHeader);

        servicio = WSClient.getInstance().getWebService();

        builder = new AlertDialog.Builder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal,menu);
        return true;
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
                                            SharedPreferences pref = getSharedPreferences(Login.PREF_KEY,0);
                                            pref.edit().clear().commit();
                                            Intent i = new Intent(Principal.this, splashLogout.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            if(!response.isSuccessful()) {
                                                Gson gson = new Gson();
                                                BadRequest errorCerrar = gson.fromJson(response.errorBody().charStream(), BadRequest.class);
                                                if(errorCerrar.getErrors() != null){
                                                    if(errorCerrar.getErrors().getUserId() != null){
                                                        Toast.makeText(Principal.this,""+errorCerrar.getErrors().getUserId(),Toast.LENGTH_LONG).show();
                                                        Log.d("Retrofit",errorCerrar.getErrors().getUserId().toString());
                                                    }
                                                    if(errorCerrar.getErrors().getUsername() != null){
                                                        Log.d("Retrofit",errorCerrar.getErrors().getUsername().toString());
                                                    }
                                                }
                                                Toast.makeText(Principal.this,errorCerrar.getMessage(),Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
