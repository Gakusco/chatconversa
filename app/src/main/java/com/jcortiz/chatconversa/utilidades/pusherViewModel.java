package com.jcortiz.chatconversa.utilidades;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.jcortiz.chatconversa.R;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataPusher;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class pusherViewModel extends AndroidViewModel {

    private MutableLiveData<DataMensaje> mensaje = new MutableLiveData<DataMensaje>();

    public pusherViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<DataMensaje> getDataMensaje() {
        pusher();
        return mensaje;
    }

    private void pusher() {
        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher("46e8ded9439a0fef8cbc", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("PUSHER","Estado actual"+ change.getCurrentState().name()+" Estado previo"+change.getPreviousState().name());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.d("PUSHER","ERROR Pusher\n"
                        +"Mensaje: "+message+ "\n"
                        +"cODIGO "+code+ "\n"
                        +"e: "+e+ "\n"
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.d("PUSHER","Nuevo mensaje: " + event.toString());
                //JSONObject json = null;

                Gson gson = new Gson();

                DataPusher dataPush = gson.fromJson(event.getData(), DataPusher.class);
                DataMensaje dataMensaje = dataPush.getMessage();
                mensaje.postValue(dataMensaje);

                //json = new JSONObject(event.toString());
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(), "Ninguno")
                        .setSmallIcon(R.drawable.ic_chat_conversa_foreground)
                        .setContentTitle("Mensaje de "+dataMensaje.getUser().getUsername())
                        .setContentText(dataMensaje.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                notificationManagerCompat.notify(4, builder.build());
            }
        });
    }
}