package com.jcortiz.chatconversa.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String imagePath;
    private String thumbnail;

    public static final int CARGAR_FOTO = 1;
    public static final int TOMAR_FOTO = 2;
    public static final int GUARDAR_FOTO = 3;

    private static final int REQUEST_PERMISSION = 10;
    private static final String[] PERMISSION_REQUIRED =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    //Header
    private View header;

    private TextView textoNombreHeader;
    private TextView textoCorreoHeader;
    private ImageButton imagenHeader;

    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

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

        header = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        inyecionDependenciaRetrofit();
        inflarComponentes();
        modificarHeader();
    }

    private boolean permisos() {
        // Permisos para sacar fotos y guardar fotos
        for(String permission : PERMISSION_REQUIRED) {
            if(ContextCompat.checkSelfPermission(Principal.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (permisos()) {
                obtenerImagen();
            } else {
                Toast.makeText(Principal.this,"Los permisos deben ser autorizados",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void modificarHeader() {
        textoNombreHeader.setText(name+" "+lastName);
        textoCorreoHeader.setText(email);
        obtenerImagen();
    }

    private void obtenerImagen() {
        Log.d("Imagen","La imagen es: "+ imagePath);
        if (imagePath != "") {
            MediaScannerConnection.scanFile(this, new String[]{imagePath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("Ruta","Path "+ imagePath);
                        }
                    });

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imagenHeader.setImageBitmap(bitmap);
        }
        imagenHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String opciones[] = {"Tomar foto","Cancelar"};
                AlertDialog.Builder alert = new AlertDialog.Builder(Principal.this);
                alert.setTitle("Seleccione una opción");
                alert.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(opciones[i] == "Subir imagen"){
                            /*Se debe corregir*/
                            //obtenerImagenDeLaGaleria();
                        }else if (opciones[i] == "Tomar foto"){
                            if(permisos()) {
                                sacarUnaFoto();
                            } else {
                                ActivityCompat.requestPermissions(Principal.this, PERMISSION_REQUIRED, REQUEST_PERMISSION);
                            }
                        }else{
                            dialogInterface.dismiss();
                        }
                    }
                });
                alert.show();
            }

        });

    }

    private void obtenerImagenDeLaGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"), CARGAR_FOTO);
    }

    private void sacarUnaFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getPackageManager()) != null){
            File fileFoto = null;

            try{
                fileFoto = crearAchivoDeImagen();
            } catch (IOException e) { }

            if (fileFoto != null) {
                /*
                Se le pasa la ruta a la aplicacion para sacar foto, con el fin de guardar la foto
                en esa ruta.
                 */
                Uri fotoURI = FileProvider.getUriForFile(Principal.this,"com.jcortiz.chatconversa.fileprovider",fileFoto);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                startActivityForResult(i, TOMAR_FOTO);
            }
        }
    }

    private File crearAchivoDeImagen() throws IOException {
        //Primero se crea un nombre de archivo de imagen
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "respaldo_" + fecha + "_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        imagePath = imagen.getAbsolutePath();
        return imagen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){



            try {
                /*
                Si se quiere buscar una foto en la galeria, entonces entrara en este if,
                si se quiere tomar una foto, entonces no entrara a este if, ya que en el caso de
                tomar una foto, ya se guardo la ruta de la foto en la variable image
                 */
                if (data.getData() != null){
                    Uri dirImagen = data.getData();
                    imagePath = dirImagen.getPath();
                    Toast.makeText(Principal.this, "Aún no se implementa subir foto", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) { e.printStackTrace(); }


            fotoConCalidad();


//            final Call<OkRequestWS> resp = servicio.cargarImagenDeUsario(token,idUser, username, image);
//            resp.enqueue(new Callback<OkRequestWS>() {
//                @Override
//                public void onResponse(Call<OkRequestWS> call, Response<OkRequestWS> response) {
//                    if(response.isSuccessful()){
//                        Uri direccion = Uri.parse(image);
//                        imagenHeader.setImageURI(direccion);
//                        Toast.makeText(Principal.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    } else if (!response.isSuccessful()) {
//                        Gson gson = new Gson();
//                        BadRequest mensajeDeError = gson.fromJson(response.errorBody().charStream(),BadRequest.class);
//                        if(mensajeDeError.getMessage() != null){
//                            Toast.makeText(Principal.this,mensajeDeError.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        if(mensajeDeError.getErrors() != null){
//                            if(mensajeDeError.getErrors().getUsername() != null){
//                                Log.d("Retrofit","Error username: "+mensajeDeError.getErrors().getUsername());
//                            }
//                            if(mensajeDeError.getErrors().getUserId() != null) {
//                                Log.d("Retrofit", "Error userId: "+mensajeDeError.getErrors().getUserId());
//                            }
//                            if(mensajeDeError.getErrors().getUserImage() != null) {
//                                Log.d("Retrofit", "Error imagen: "+mensajeDeError.getErrors().getUserImage());
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<OkRequestWS> call, Throwable t) {
//
//                }
//            });
        }
    }

    private void fotoConCalidad() {
        int targetW = imagenHeader.getWidth();
        int targetH = imagenHeader.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int scale = (int) targetW/targetH;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imagenHeader.setImageBitmap(bitmap);

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
        imagePath = preferencias.getString("image","errorImage");
        thumbnail = preferencias.getString("thumbnail","errorThumbnail");

        textoCorreoHeader = header.findViewById(R.id.textEmailHeader);
        textoNombreHeader = header.findViewById(R.id.textNombreHeader);
        imagenHeader = header.findViewById(R.id.imagenUsuarioHeader);

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
