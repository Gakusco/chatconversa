package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.clasesDeError.BadRequest;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.EnviarMensajeWS;

import java.io.File;
import java.util.ArrayList;

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

    private ImageButton btnAdjuntar;
    private ImageButton btnEnviarMensaje;
    private TextInputEditText editTextContenidoMensaje;

    private ArrayList<DataMensaje> mensajes;

    private SharedPreferences preferencias;

    private WebService servicio;

    private InicioViewModel inicioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        inflarComponentes(root);

        actualizarLista();
        enviarMensaje();
        adjuntar();
        return root;
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
        AlertDialog alertDialog;
        AlertDialog.Builder builder;
        View opcionesAdjuntar;

        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        opcionesAdjuntar = LayoutInflater.from(getContext()).inflate(R.layout.flotante_adjuntar,null);

        builder.setView(opcionesAdjuntar);
        alertDialog = builder.create();
        alertDialog.show();
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
                if (i == EditorInfo.IME_ACTION_SEND) {
                    peticionDeEnvioDeMensaje();

                    flag = true;
                }
                return flag;
            }

        });

        comprobarInputEditText();

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                peticionDeEnvioDeMensaje();
            }
        });
    }

    private void comprobarInputEditText() {
        editTextContenidoMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    btnAdjuntar.setVisibility(View.GONE);
                    btnEnviarMensaje.setVisibility(View.VISIBLE);
                } else {
                    btnAdjuntar.setVisibility(View.VISIBLE);
                    btnEnviarMensaje.setVisibility(View.GONE);
                }
            }
        });
    }

    private void peticionDeEnvioDeMensaje() {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),user_id);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),username);
        RequestBody message = RequestBody.create(MediaType.parse("text/plain"),editTextContenidoMensaje.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"),"");

        File archivoImg = new File("");
        RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"),archivoImg);
        MultipartBody.Part archivo = MultipartBody.Part.createFormData("user_image",archivoImg.getName(),img);

        final Call<EnviarMensajeWS> resp = servicio.enviarMensaje(token,null,id,user,message,null,null);
        resp.enqueue(new Callback<EnviarMensajeWS>() {
            @Override
            public void onResponse(Call<EnviarMensajeWS> call, Response<EnviarMensajeWS> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit","Mensaje enviado con exito: "+response.body().toString());
                    editTextContenidoMensaje.setText("");
                    actualizarLista();
                } else if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    BadRequest mensajeDeError = gson.fromJson(response.errorBody().charStream(),BadRequest.class);
                    Log.d("Retrofit",mensajeDeError.getErrors().toString());
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
    }

    private void inflarComponentes(View root) {
        servicio = WSClient.getInstance().getWebService();
        listaDeMensajes = root.findViewById(R.id.recyclerView);
        btnAdjuntar = root.findViewById(R.id.btnAdjuntar);
        btnEnviarMensaje = root.findViewById(R.id.btnEnviarMensaje);
        editTextContenidoMensaje = root.findViewById(R.id.editTextContenidoMensaje);
        mensajes = new ArrayList<>();
        preferencias = getActivity().getSharedPreferences(Login.PREF_KEY, Principal.MODE_PRIVATE);
        user_id = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
    }

}
