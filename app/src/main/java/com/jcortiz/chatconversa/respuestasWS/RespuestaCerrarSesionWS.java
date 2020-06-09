package com.jcortiz.chatconversa.respuestasWS;

import java.util.Objects;

public class RespuestaCerrarSesionWS {
    private int statusCode;
    private String message;

    public RespuestaCerrarSesionWS(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespuestaCerrarSesionWS that = (RespuestaCerrarSesionWS) o;
        return statusCode == that.statusCode &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, message);
    }

    @Override
    public String toString() {
        return "RespuestaCerrarSesionWS{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
