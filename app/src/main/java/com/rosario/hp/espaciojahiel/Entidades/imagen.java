package com.rosario.hp.espaciojahiel.Entidades;

public class imagen {
    private String id;
    private String nombre;
    private  String fecha_alta;
    private String descripcion;
    private String precio;
    private String calificacion;

    public imagen(String id, String nombre, String fecha_alta, String descripcion, String precio, String calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_alta = fecha_alta;
        this.descripcion = descripcion;
        this.precio = precio;
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }
}
