package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.jcortiz.chatconversa.Retrofit.WSClient;
import com.jcortiz.chatconversa.Retrofit.WebService;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MensajesRepository {
    private WebService servicio;
    private MutableLiveData<ArrayList<DataMensaje>> listaMensajes = new MutableLiveData<>();

    public MensajesRepository() {
        servicio = WSClient.getInstance().getWebService();
    }

    public MutableLiveData<ArrayList<DataMensaje>> getListaMensajes(String token, String userId, String username) {
        Call<MensajeWS> resp = servicio.obtenerMensajes(token, userId, username);

        resp.enqueue(new Callback<MensajeWS>() {
            @Override
            public void onResponse(Call<MensajeWS> call, Response<MensajeWS> response) {
                if(response.isSuccessful()) {
                    listaMensajes.setValue(response.body().getData());
                    Log.d("Retrofit", response.body().toString());
                } else {
                    Log.d("Retrofit", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MensajeWS> call, Throwable t) {
                Log.d("Retrofit", "Se ha producido un error: "+t.getMessage());
                listaMensajes.postValue(null);
            }
        });

        return listaMensajes;
    }

}
