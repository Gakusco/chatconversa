package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;

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

    private Button btnEnviarMensaje;
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
        return root;
    }

    private void actualizarLista() {
        inicioViewModel.getDataMensaje(token, user_id, username).observe(getViewLifecycleOwner(), chatResponse -> {
            mensajes = chatResponse;
            desplegarLista();
        });

    }

    private void enviarMensaje() {
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
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
        });
    }

    private void desplegarLista() {
        // Invierte el orden de la lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);

        listaDeMensajes.setLayoutManager(linearLayoutManager);
        adaptador = new Adaptador(getContext(),mensajes);
        listaDeMensajes.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Toast.makeText(getContext(),mensajes.get(listaDeMensajes.getChildAdapterPosition(view)).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inflarComponentes(View root) {
        servicio = WSClient.getInstance().getWebService();
        listaDeMensajes = root.findViewById(R.id.recyclerView);
        btnEnviarMensaje = root.findViewById(R.id.btnEnviarMensaje);
        editTextContenidoMensaje = root.findViewById(R.id.editTextContenidoMensaje);
        mensajes = new ArrayList<>();
        preferencias = getActivity().getSharedPreferences(Login.PREF_KEY, Principal.MODE_PRIVATE);
        user_id = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
    }

}
