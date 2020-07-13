package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcortiz.chatconversa.Activities.Login;
import com.jcortiz.chatconversa.Activities.Principal;
import com.jcortiz.chatconversa.Adaptador;
import com.jcortiz.chatconversa.Constantes;
import com.jcortiz.chatconversa.Mensaje;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.OkRequestWS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    private Adaptador adaptador;
    private RecyclerView listaDeMensajes;
    //private ArrayList<Mensaje> mensajes;

    private String user_id;
    private String username;
    private String token;

    private ArrayList<DataMensaje> mensajes;

    private SharedPreferences preferencias;

    private WebService servicio;

    private InicioViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        inflarComponentes(root);
        obtenerServicio();
        obtenerDatos();
        return root;
    }

    private void obtenerDatos() {
        final Call<MensajeWS> resp = servicio.obtenerMensajes(token, user_id, username);

        resp.enqueue(new Callback<MensajeWS>() {
            @Override
            public void onResponse(Call<MensajeWS> call, Response<MensajeWS> response) {
                if(response.isSuccessful()) {
                    Log.d("Retrofit","Respuesta correcta: "+response.body().toString());
                    obtenerDatos();
                    agregarDatosALista(response.body().getData());
                    desplegarLista();
                } else if (!response.isSuccessful()) {
                    Log.d("Retrofit","Respuesta incorrecta: "+response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MensajeWS> call, Throwable t) {
                Log.d("Retrofit", "Mensaje de error: "+t.getMessage());
            }
        });
    }

    private void desplegarLista() {
        listaDeMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new Adaptador(getContext(),mensajes);
        listaDeMensajes.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Toast.makeText(getContext(),mensajes.get(listaDeMensajes.getChildAdapterPosition(view)).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarDatosALista(ArrayList<DataMensaje> listaMensajes) {
        mensajes = listaMensajes;
    }

    private void inflarComponentes(View root) {
        listaDeMensajes = root.findViewById(R.id.recyclerView);
        mensajes = new ArrayList<>();
        preferencias = getActivity().getSharedPreferences(Login.PREF_KEY, Principal.MODE_PRIVATE);
        user_id = preferencias.getString(Constantes.USER_ID,Constantes.ERROR_USER_ID);
        username = preferencias.getString(Constantes.USER,Constantes.ERROR_USER);
        token = "Bearer "+preferencias.getString(Constantes.TOKEN,Constantes.ERROR_TOKEN);
    }

    private void obtenerServicio() {
        servicio = WSClient.getInstance().getWebService();
    }
}
