package com.jcortiz.chatconversa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

class cerrarSesionErrors {

    private List<String> username;

    private List<String> userId;

    public cerrarSesionErrors(List<String> username, List<String> userId) {
        this.username = username;
        this.userId = userId;
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cerrarSesionErrors that = (cerrarSesionErrors) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userId);
    }

    @Override
    public String toString() {
        return "cerrarSesionErrors{" +
                "username=" + username +
                ", userId=" + userId +
                '}';
    }
}
