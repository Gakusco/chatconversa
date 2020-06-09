package com.jcortiz.chatconversa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class errorCerrarSesion {
    @SerializedName("status_code")
    @Expose
    private int codeStatus;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private cerrarSesionErrors errors;

    public errorCerrarSesion(int codeStatus, String message, cerrarSesionErrors errors) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        errorCerrarSesion that = (errorCerrarSesion) o;
        return codeStatus == that.codeStatus &&
                Objects.equals(message, that.message) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public String toString() {
        return "errorCerrarSesion{" +
                "codeStatus=" + codeStatus +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeStatus, message, errors);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public cerrarSesionErrors getErrors() {
        return errors;
    }

    public void setErrors(cerrarSesionErrors errors) {
        this.errors = errors;
    }
}
