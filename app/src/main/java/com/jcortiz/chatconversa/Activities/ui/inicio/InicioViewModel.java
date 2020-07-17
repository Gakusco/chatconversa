package com.jcortiz.chatconversa.Activities.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;

import java.util.ArrayList;
import java.util.List;

public class InicioViewModel extends ViewModel {

    private MensajesRepository mensajesRepository;
    private MutableLiveData<ArrayList<DataMensaje>> dataMensaje;

    public InicioViewModel () {
        mensajesRepository = new MensajesRepository();
    }

    public MutableLiveData<ArrayList<DataMensaje>> getDataMensaje(String token, String userId, String username) {
        dataMensaje = mensajesRepository.getListaMensajes(token, userId, username);
        return dataMensaje;
    }
}