package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPusher {
    @SerializedName("message")
    @Expose
    private DataMensaje message;

    public DataPusher(DataMensaje message) {
        this.message = message;
    }

    public DataMensaje getMessage() {
        return message;
    }

    public void setMessage(DataMensaje message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "message=" + message +
                '}';
    }
}
