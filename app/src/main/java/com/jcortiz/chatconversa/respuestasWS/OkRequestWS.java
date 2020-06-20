package com.jcortiz.chatconversa.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OkRequestWS {
    @SerializedName("status_code")
    @Expose
    private int code;
    private String message;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("data")
    @Expose
    private User user;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OkRequestWS that = (OkRequestWS) o;
        return code == that.code &&
                Objects.equals(message, that.message) &&
                Objects.equals(token, that.token) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, token, user);
    }

    @Override
    public String toString() {
        return "RespuestaLoginWS{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
