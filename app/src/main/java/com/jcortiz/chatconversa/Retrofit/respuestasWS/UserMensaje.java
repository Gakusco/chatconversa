package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMensaje {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_thumbnail")
    @Expose
    private String userThumbnail;

    public UserMensaje(int userId, String username, String userImage, String userThumbnail) {
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
        this.userThumbnail = userThumbnail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserThumbnail() {
        return userThumbnail;
    }

    public void setUserThumbnail(String userThumbnail) {
        this.userThumbnail = userThumbnail;
    }

    @Override
    public String toString() {
        return "UserMensaje{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userThumbnail='" + userThumbnail + '\'' +
                '}';
    }
}
