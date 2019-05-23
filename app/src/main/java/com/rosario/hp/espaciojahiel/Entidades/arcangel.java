package com.rosario.hp.espaciojahiel.Entidades;

public class arcangel {

    private String id;
    private String titulo;
    private String mensaje;
    private String dia;

    public arcangel(String id, String titulo, String mensaje, String dia) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.dia = dia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
