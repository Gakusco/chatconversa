package com.jcortiz.chatconversa.Retrofit.clasesDeError;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Errors {
    @SerializedName("name")
    @Expose
    private List<String> name;
    @SerializedName("lastname")
    @Expose
    private List<String> lastname;
    @SerializedName("run")
    @Expose
    private List<String> run;
    @SerializedName("username")
    @Expose
    private List<String> username;
    @SerializedName("email")
    @Expose
    private List<String> email;
    @SerializedName("password")
    @Expose
    private List<String> password;
    @SerializedName("token_enterprise")
    @Expose
    private List<String> tokenEnterprise;
    @SerializedName("user_id")
    @Expose
    private List<String> userId;
    @SerializedName("user_image")
    @Expose
    private List<String> userImage;

    public Errors(List<String> name, List<String> lastname, List<String> run, List<String> username, List<String> email, List<String> password, List<String> tokenEnterprise, List<String> userId, List<String> userImage) {
        this.name = name;
        this.lastname = lastname;
        this.run = run;
        this.username = username;
        this.email = email;
        this.password = password;
        this.tokenEnterprise = tokenEnterprise;
        this.userId = userId;
        this.userImage = userImage;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getLastname() {
        return lastname;
    }

    public void setLastname(List<String> lastname) {
        this.lastname = lastname;
    }

    public List<String> getRun() {
        return run;
    }

    public void setRun(List<String> run) {
        this.run = run;
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getTokenEnterprise() {
        return tokenEnterprise;
    }

    public void setTokenEnterprise(List<String> tokenEnterprise) {
        this.tokenEnterprise = tokenEnterprise;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public List<String> getUserImage() {
        return userImage;
    }

    public void setUserImage(List<String> userImage) {
        this.userImage = userImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Errors errors = (Errors) o;
        return Objects.equals(name, errors.name) &&
                Objects.equals(lastname, errors.lastname) &&
                Objects.equals(run, errors.run) &&
                Objects.equals(username, errors.username) &&
                Objects.equals(email, errors.email) &&
                Objects.equals(password, errors.password) &&
                Objects.equals(tokenEnterprise, errors.tokenEnterprise) &&
                Objects.equals(userId, errors.userId) &&
                Objects.equals(userImage, errors.userImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastname, run, username, email, password, tokenEnterprise, userId, userImage);
    }

    @Override
    public String toString() {
        return "Errors{" +
                "name=" + name +
                ", lastname=" + lastname +
                ", run=" + run +
                ", username=" + username +
                ", email=" + email +
                ", password=" + password +
                ", tokenEnterprise=" + tokenEnterprise +
                ", userId=" + userId +
                ", userImage=" + userImage +
                '}';
    }
}
