package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EnviarMensajeWS {
    @SerializedName("status_code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataMensaje data;

    public EnviarMensajeWS(int code, String message, DataMensaje data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataMensaje getData() {
        return data;
    }

    public void setData(DataMensaje data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EnviarMensajeWS{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
