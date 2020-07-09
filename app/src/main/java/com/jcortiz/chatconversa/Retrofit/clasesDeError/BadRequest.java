package com.jcortiz.chatconversa.Retrofit.clasesDeError;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class BadRequest {
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    public BadRequest(Integer statusCode, String message, Errors errors) {
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

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadRequest that = (BadRequest) o;
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
        return "BadRequest{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
