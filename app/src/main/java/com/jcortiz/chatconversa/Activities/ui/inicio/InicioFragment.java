package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jcortiz.chatconversa.Activities.Login;
import com.jcortiz.chatconversa.Activities.Principal;
import com.jcortiz.chatconversa.Adaptador;
import com.jcortiz.chatconversa.Constantes;
import com.jcortiz.chatconversa.Activities.MapsActivity;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.EnviarMensajeWS;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    private Adaptador adaptador;
    private RecyclerView listaDeMensajes;

    private String user_id;
    private String username;
    private String token;
    private String path;
    private String contenidoMensaje;
    private final int CODE_PHOTO = 30;
    private final int CODE_CAMERA = 100;

    private ImageButton btnAdjuntar;
    private ImageButton btnEnviarMensaje;

    private ImageButton btnObtenerFoto;
    private ImageButton btnObtenerImagenGaleria;
    private ImageButton btnObtenerUbicacion;

    private TextInputEditText editTextContenidoMensaje;

    private ArrayList<DataMensaje> mensajes;

    private SharedPreferences preferencias;

    private WebService servicio;

    private InicioViewModel inicioViewModel;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private ProgressBar progressBar;

    private static final int GALLERY_PERMISSION = 10;
    private static final int CAMERA_PERMISSION = 20;
    private static final String[] PERMISSION_REQUIRED =
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        inflarComponentes(root);

        actualizarLista();
        enviarMensaje();
        adjuntar();
        comprobarEnvioUbicacion();
        return root;
    }

    private void comprobarEnvioUbicacion() {
        Bundle ubicacion = getActivity().getIntent().getExtras();
        if(ubicacion != null) {
            String latitud = ubicacion.getString(Constantes.ENVIAR_LATITUD);
            String longitud = ubicacion.getString(Constantes.ENVIAR_LONGITUD);
            contenidoMensaje = "";
            peticionDeEnvioDeMensaje("",longitud,latitud);
        }
    }

    private boolean permisoGaleria() {
        // Permisos para acceder a la galeria
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private boolean permisoFoto(){
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    private void adjuntar() {
        btnAdjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplegarOpcionesAdjuntar();
            }
        });
    }

    private void desplegarOpcionesAdjuntar() {
        View opcionesAdjuntar;

        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        opcionesAdjuntar = LayoutInflater.from(getContext()).inflate(R.layout.flotante_adjuntar,null);
        btnObtenerImagenGaleria = opcionesAdjuntar.findViewById(R.id.btnGaleria);
        btnObtenerFoto = opcionesAdjuntar.findViewById(R.id.btnFoto);
        btnObtenerUbicacion = opcionesAdjuntar.findViewById(R.id.btnUbicacion);

        builder.setView(opcionesAdjuntar);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        alertDialog.show();

        obtenerFoto();
        obtenerImagenGaleria();
        obtenerUbicacion();
    }

    private void obtenerUbicacion() {
        btnObtenerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                startActivity(i);
            }
        });
    }

    private void obtenerFoto() {
        btnObtenerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permisoFoto()) {
                    sacarUnaFoto();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),PERMISSION_REQUIRED,CAMERA_PERMISSION);
                }
            }
        });
    }

    private void sacarUnaFoto() {
        Intent i =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            File fileFoto = null;

            try {
                fileFoto = generarRuta();
            } catch (IOException e ) {}

            if ( fileFoto != null ) {
                Uri fotoURI = FileProvider.getUriForFile(getContext(), "com.jcortiz.chatconversa.fileprovider",fileFoto);
                i.putExtra(MediaStore.EXTRA_OUTPUT,fotoURI);
                startActivityForResult(i, CODE_CAMERA);
            }
        }
    }

    private File generarRuta() throws IOException {
        // Primero se crea un nombre de archivo
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "respaldo_"+fecha + "_";
        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg", directorio);

        path = imagen.getAbsolutePath();
        return imagen;
    }

    private void obtenerImagenGaleria() {
        btnObtenerImagenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permisoGaleria()){
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/");
                    startActivityForResult(i.createChooser(i,"Seleccione una aplicacion"),CODE_PHOTO);

                } else {
                    ActivityCompat.requestPermissions(getActivity(),PERMISSION_REQUIRED, GALLERY_PERMISSION);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            if(requestCode == CODE_PHOTO) {
                Uri imagenUri = data.getData();
                path = obtenerPath(imagenUri);
                previsualizarImagen();
            } else if(requestCode == CODE_CAMERA) {
                previsualizarImagen();
            }
        }
    }

    private void previsualizarImagen() {
        obtenerImagenGaleria();
        alertDialog.dismiss();
        View imagenYTexto;
        Button enviarImagen;
        EditText comentarioDeLaImagen;
        ImageView verImagen;

        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        imagenYTexto = LayoutInflater.from(getContext()).inflate(R.layout.flotante_previsualizar,null);
        enviarImagen = imagenYTexto.findViewById(R.id.btnEnviarImagen);
        comentarioDeLaImagen = imagenYTexto.findViewById(R.id.editTextComentarioImagen);
        verImagen = imagenYTexto.findViewById(R.id.imagenPrevisualizar);
        imagenConCalidad(verImagen);

        builder.setView(imagenYTexto);
        alertDialog = builder.create();
        alertDialog.show();
        enviarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contenidoMensaje = comentarioDeLaImagen.getText().toString();
                peticionDeEnvioDeMensaje(path,"","");
                alertDialog.dismiss();
            }
        });
    }

    private void imagenConCalidad(ImageView imageView) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int scale = 1;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imageView.setImageBitmap(bitmap);

    }

    private String obtenerPath(Uri data) {
        String resultado = "";
        Cursor cursor = getContext().getContentResolver().query(data, null,null, null,null);
        if (cursor == null){
            resultado = data.getPath();
        } else if (cursor.moveToFirst()) {
            int indice = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            resultado = cursor.getString(indice);
            cursor.close();
        }
        return resultado;
    }

    private void actualizarLista() {
        inicioViewModel.getDataMensaje(token, user_id, username).observe(getViewLifecycleOwner(), chatResponse -> {
            mensajes = chatResponse;
            desplegarLista();
        });

    }

    private void enviarMensaje() {
        editTextContenidoMensaje.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextContenidoMensaje.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean flag = false;
                if (i == EditorInfo.IME_ACTION_SEND && editTextContenidoMensaje.getText().length()>0) {
                    contenidoMensaje = editTextContenidoMensaje.getText().toString();
                    peticionDeEnvioDeMensaje("","","");

                    flag = true;
                } else {
                    Toast.makeText(getContext(),"El mensaje está vacío",Toast.LENGTH_SHORT).show();
                }
                return flag;
            }

        });
    }


    private void peticionDeEnvioDeMensaje(String path, String longi, String lati) {
        progressBar.setVisibility(View.VISIBLE);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),user_id);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),username);
        RequestBody message = RequestBody.create(MediaType.parse("text/plain"),contenidoMensaje);

        RequestBody longitude;
        RequestBody latitude;
        if(longi.isEmpty()) {
            longitude = null;
            latitude = null;
        } else {
            latitude = RequestBody.create(MediaType.parse("text/plain"),lati);
            longitude = RequestBody.create(MediaType.parse("text/plain"),longi);
        }


        File archivoImg = new File(path);
        RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"),archivoImg);
        MultipartBody.Part archivo;
        if(path.isEmpty()) {
            archivo = null;
        } else {
            archivo = MultipartBody.Part.createFormData("image",archivoImg.getName(),img);
        }

        final Call<EnviarMensajeWS> resp = servicio.enviarMensaje(token,archivo,id,user,message,latitude,longitude);
        resp.enqueue(new Callback<EnviarMensajeWS>() {
            @Override
            public void onResponse(Call<EnviarMensajeWS> call, Response<EnviarMensajeWS> response) {
                if (response.isSuccessful()) {
                    editTextContenidoMensaje.setText("");
                    actualizarLista();
                } else if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    BadRequest mensajeDeError = gson.fromJson(response.errorBody().charStream(),BadRequest.class);
                    Log.d("Retrofit",mensajeDeError.getMessage());
                    if(mensajeDeError.getErrors() != null) {
                        Toast.makeText(getContext(),mensajeDeError.getErrors().getImagen().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EnviarMensajeWS> call, Throwable t) {
                Log.d("Retrofit","Error al enviar mensaje: "+t.getMessage());
            }
        });
    }

    private void desplegarLista() {
        // Invierte el orden de la lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);

        listaDeMensajes.setLayoutManager(linearLayoutManager);
        adaptador = new Adaptador(getContext(),mensajes, user_id);
        listaDeMensajes.setAdapter(adaptador);
        progressBar.setVisibility(View.GONE);
        listaDeMensajes.setVisibility(View.VISIBLE);
    }

    private void inflarComponentes(View root) {
        servicio = WSClient.getInstance().getWebService();
        progressBar = root.findViewById(R.id.progressBar);
        listaDeMensajes = root.findViewById(R.id.recyclerView);
        btnAdjuntar = root.findViewById(R.id.btnAdjuntar);
        editTextContenidoMensaje = root.findViewById(R.id.editTextContenidoMensaje);
        mensajes = new ArrayList<>();
        preferencias = getActivity().getSharedPreferences(Login.PREF_KEY, Principal.MODE_PRIVATE);
        user_id = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
    }

}
