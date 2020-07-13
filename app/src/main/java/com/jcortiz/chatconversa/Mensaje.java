package com.jcortiz.chatconversa;

public class Mensaje {
    private String foto;
    private String nombre;
    private String cuerpoMensaje;
    private String horaMensaje;

    public Mensaje(String foto, String nombre, String cuerpoMensaje, String horaMensaje) {
        this.foto = foto;
        this.nombre = nombre;
        this.cuerpoMensaje = cuerpoMensaje;
        this.horaMensaje = horaMensaje;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuerpoMensaje() {
        return cuerpoMensaje;
    }

    public void setCuerpoMensaje(String cuerpoMensaje) {
        this.cuerpoMensaje = cuerpoMensaje;
    }

    public String getHoraMensaje() {
        return horaMensaje;
    }

    public void setHoraMensaje(String horaMensaje) {
        this.horaMensaje = horaMensaje;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "foto='" + foto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cuerpoMensaje='" + cuerpoMensaje + '\'' +
                ", horaMensaje='" + horaMensaje + '\'' +
                '}';
    }
}
