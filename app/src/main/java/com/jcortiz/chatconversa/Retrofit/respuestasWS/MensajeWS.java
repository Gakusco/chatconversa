package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MensajeWS {
    @SerializedName("status_code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<DataMensaje> data;

    public MensajeWS(int code, String message, ArrayList<DataMensaje> data) {
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

    public ArrayList<DataMensaje> getData() {
        return data;
    }

    public void setData(ArrayList<DataMensaje> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MensajeWS{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
