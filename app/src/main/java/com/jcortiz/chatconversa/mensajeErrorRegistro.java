package com.jcortiz.chatconversa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class mensajeErrorRegistro {
    private Integer statusCode;
    private String message;

    @SerializedName("errors")
    @Expose
    private userErrors errors;

    public mensajeErrorRegistro(Integer statusCode, String message, userErrors errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public userErrors getErrors() {
        return errors;
    }

    public void setErrors(userErrors errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        mensajeErrorRegistro that = (mensajeErrorRegistro) o;
        return Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(message, that.message) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, message, errors);
    }

    @Override
    public String toString() {
        return "mensajeErrorRegistro{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
