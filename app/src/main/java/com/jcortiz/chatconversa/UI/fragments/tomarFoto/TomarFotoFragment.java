package com.jcortiz.chatconversa.UI.fragments.tomarFoto;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.google.gson.Gson;
import com.jcortiz.chatconversa.UI.Login;
import com.jcortiz.chatconversa.UI.Principal;
import com.jcortiz.chatconversa.utilidades.Constantes;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.OkRequestWS;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TomarFotoFragment extends Fragment {

    private Button tomarFotoBtn;
    private Button guardarFoto;
    private ImageView fotoImageView;
    private TextView textExitoso;
    private ProgressBar progressBar;

    private static WebService servicio;
    private SharedPreferences preferencias;

    private String imagePath;

    private String token;
    private String idUser;
    private String username;
    private String imagenUser;
    private String thumbnail;

    public static final int TOMAR_FOTO = 2;

    private static final int REQUEST_PERMISSION = 10;
    private static final String[] PERMISSION_REQUIRED =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tomar_foto, container, false);

        inflarComponentes(root);
        obtenerPreferencias();
        mostrarFotoActual();
        obtenerFoto();
        return root;
    }

    private void inflarComponentes(View root) {
        fotoImageView = root.findViewById(R.id.fotoImageView);
        tomarFotoBtn = root.findViewById(R.id.tomarFotoBtn);
        guardarFoto = root.findViewById(R.id.guardarFotoBtn);
        textExitoso = root.findViewById(R.id.textoExitoso);
        progressBar = root.findViewById(R.id.progressBar);
        servicio = WSClient.getInstance().getWebService();
    }

    private void mostrarFotoActual() {
        if(imagenUser != Constantes.ERROR_IMAGE && imagenUser !=null && !imagenUser.isEmpty()) {
            Picasso.get().load(imagenUser).into(fotoImageView);
        }
    }

    private boolean permisos() {
        // Permisos para sacar fotos y guardar fotos
        for(String permission : PERMISSION_REQUIRED) {
            if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void obtenerFoto() {
        guardarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath != null){
                    tomarFotoBtn.setVisibility(View.GONE);
                    guardarFoto.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    subirFoto();
                } else {
                    Toast.makeText(getContext(), "Debe tomar una foto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tomarFotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permisos()) {
                    sacarUnaFoto();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSION_REQUIRED, REQUEST_PERMISSION);
                }
            }
        });
    }

    private void subirFoto() {
        File archivoImg = new File(imagePath);

        RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"),archivoImg);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),idUser);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),username);

        MultipartBody.Part archivo = MultipartBody.Part.createFormData("user_image",archivoImg.getName(),img);

        final Call<OkRequestWS> resp = servicio.cargarImagenDeUsario(token,archivo,id,user);
        resp.enqueue(new Callback<OkRequestWS>() {
            @Override
            public void onResponse(Call<OkRequestWS> call, Response<OkRequestWS> response) {
                if(response.isSuccessful()){
                    guardarImagenEnPreferencias(response.body());

                    progressBar.setVisibility(View.GONE);
                    textExitoso.setVisibility(View.VISIBLE);

                    ((Principal)getActivity()).inflarComponentes();
                    ((Principal)getActivity()).modificarHeader();
                } else if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    BadRequest mensajeDeError = gson.fromJson(response.errorBody().charStream(),BadRequest.class);
                    respuestaDeError(mensajeDeError);
                }
            }

            @Override
            public void onFailure(Call<OkRequestWS> call, Throwable t) {
                Log.d("ERROR","msg: "+t.getMessage());
            }
        });
    }

    private void guardarImagenEnPreferencias(OkRequestWS body) {
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(Constantes.IMAGE,body.getUser().getImage());
        editor.putString(Constantes.THUMBNAIL,body.getUser().getThumbnail());
        editor.commit();
    }

    private void respuestaDeError(BadRequest mensajeDeError) {
        if(mensajeDeError.getErrors().getImagen().toString() != null){
            Toast.makeText(getActivity(),mensajeDeError.getErrors().getImagen().toString(), Toast.LENGTH_SHORT).show();
        }
        if(mensajeDeError.getErrors() != null){
            if(mensajeDeError.getErrors().getUsername() != null){
                Log.d("Retrofit","Error username: "+mensajeDeError.getErrors().getUsername());
            }
            if(mensajeDeError.getErrors().getUserId() != null) {
                Log.d("Retrofit", "Error userId: "+mensajeDeError.getErrors().getUserId());
            }
            if(mensajeDeError.getErrors().getUserImage() != null) {
                Log.d("Retrofit", "Error imagen: "+mensajeDeError.getErrors().getUserImage());
            }
        }
        progressBar.setVisibility(View.GONE);
        tomarFotoBtn.setVisibility(View.VISIBLE);
    }

    private void sacarUnaFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getActivity().getPackageManager()) != null){
            File fileFoto = null;

            try{
                fileFoto = crearAchivoDeImagen();
            } catch (IOException e) { }

            if (fileFoto != null) {
                /*
                Se le pasa la ruta a la aplicacion para sacar foto, con el fin de guardar la foto
                en esa ruta.
                 */
                Uri fotoURI = FileProvider.getUriForFile(getContext(),"com.jcortiz.chatconversa.fileprovider",fileFoto);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                startActivityForResult(i, TOMAR_FOTO);
            }
        }
    }

    private File crearAchivoDeImagen() throws IOException {
        //Primero se crea un nombre de archivo de imagen
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "respaldo_" + fecha + "_";
        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        imagePath = imagen.getAbsolutePath();
        Log.d("Retrofit",imagePath);
        return imagen;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Principal.RESULT_OK){
            fotoConCalidad();
        }
    }

    private void fotoConCalidad() {
        int targetW = fotoImageView.getWidth();
        int targetH = fotoImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int scale = (int) targetW/targetH;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        fotoImageView.setImageBitmap(bitmap);

    }

    private void obtenerPreferencias() {
        preferencias = getActivity().getSharedPreferences(Login.PREF_KEY,Principal.MODE_PRIVATE);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
        idUser = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        imagenUser = preferencias.getString(Constantes.IMAGE,Constantes.ERROR_IMAGE);
        thumbnail = preferencias.getString(Constantes.THUMBNAIL,Constantes.ERROR_THUMBNAIL);
    }
}
