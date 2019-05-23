package com.rosario.hp.espaciojahiel.Entidades;

public class evento {

    private String id;
    private String fecha;
    private String nombre;
    private String link;
    private String fecha_alta;
    private String lugar;
    private String hora;
    private String destacado;

    public evento(String id, String fecha, String nombre, String link, String fecha_alta, String lugar) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.link = link;
        this.fecha_alta = fecha_alta;
        this.lugar = lugar;
    }

    public String getId() {
        return id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDestacado() {
        return destacado;
    }

    public void setDestacado(String destacado) {
        this.destacado = destacado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }
}
