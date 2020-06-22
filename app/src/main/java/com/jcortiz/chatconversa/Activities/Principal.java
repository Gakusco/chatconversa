package com.jcortiz.chatconversa.Activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.WebService;
import com.jcortiz.chatconversa.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.respuestasWS.OkRequestWS;
import com.jcortiz.chatconversa.splashes.splashLogout;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private String image;
    private String thumbnail;


    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog.Builder builder;

    private TextView textoNombre;
    private TextView textoApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        inyecionDependenciaRetrofit();
        inflarComponentes();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void inflarComponentes() {
        preferencias = getSharedPreferences(Login.PREF_KEY,MODE_PRIVATE);
        token = "Bearer "+preferencias.getString("token","errorToken");
        idUser = preferencias.getString("userId","errorId");
        name = preferencias.getString("name","errorName");
        lastName = preferencias.getString("lastName","errorLastname");
        username = preferencias.getString("user","errorUser");
        run = preferencias.getString("run","errorRun");
        email = preferencias.getString("email","errorEmail");
        image = preferencias.getString("image","errorImage");
        thumbnail = preferencias.getString("thumbnail","errorThumbnail");

        builder = new AlertDialog.Builder(this);
    }

    private void inyecionDependenciaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(WebService.class);
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
