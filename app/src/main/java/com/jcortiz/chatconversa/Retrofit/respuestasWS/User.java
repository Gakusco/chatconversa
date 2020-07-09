package com.jcortiz.chatconversa.Retrofit.respuestasWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("run")
    @Expose
    private String run;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public User(Integer id, String name, String lastName, String username, String run, String email, String image, String thumbnail) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.run = run;
        this.email = email;
        this.image = image;
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(username, user.username) &&
                Objects.equals(run, user.run) &&
                Objects.equals(email, user.email) &&
                Objects.equals(image, user.image) &&
                Objects.equals(thumbnail, user.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, username, run, email, image, thumbnail);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", run='" + run + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
