package com.jcortiz.chatconversa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class mensajeErrorLogin {
    @SerializedName("status_code")
    @Expose
    private int codeStatus;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private loginErrors errors;

    public mensajeErrorLogin(int codeStatus, String message, loginErrors errors) {
        this.codeStatus = codeStatus;
        this.message = message;
        this.errors = errors;
    }

    public int getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(int codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public loginErrors getErrors() {
        return errors;
    }

    public void setErrors(loginErrors errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        mensajeErrorLogin that = (mensajeErrorLogin) o;
        return codeStatus == that.codeStatus &&
                Objects.equals(message, that.message) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeStatus, message, errors);
    }

    @Override
    public String toString() {
        return "mensajeErrorLogin{" +
                "codeStatus=" + codeStatus +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
