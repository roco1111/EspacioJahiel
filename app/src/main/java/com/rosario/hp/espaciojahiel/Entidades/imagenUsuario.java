package com.rosario.hp.espaciojahiel.Entidades;

public class imagenUsuario {
    private String id_imagen;
    private String id_usuario;
    private String calificacion;

    public imagenUsuario(String id_imagen, String id_usuario, String calificacion) {
        this.id_imagen = id_imagen;
        this.id_usuario = id_usuario;
        this.calificacion = calificacion;
    }

    public String getId_imagen() {
        return id_imagen;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public void setId_imagen(String id_imagen) {
        this.id_imagen = id_imagen;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
