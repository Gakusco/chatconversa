package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMensaje {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("thumbnail")
    @Expose
    private String thumnail;
    @SerializedName("user")
    @Expose
    private UserMensaje user;

    public DataMensaje(int id, String date, String message, String latitude, String longitude, String image, String thumnail, UserMensaje user) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.thumnail = thumnail;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public UserMensaje getUser() {
        return user;
    }

    public void setUser(UserMensaje user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DataMensaje{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", image='" + image + '\'' +
                ", thumnail='" + thumnail + '\'' +
                ", user=" + user +
                '}';
    }
}
