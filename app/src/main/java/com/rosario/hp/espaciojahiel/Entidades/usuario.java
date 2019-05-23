package com.rosario.hp.espaciojahiel.Entidades;

public class usuario {
    private String id;
    private String nombre;
    private String mail;
    private String estado;
    private String clave;
    private String firebase;
    private String fecha_nac;

    public usuario(String id, String nombre, String mail, String estado, String clave, String firebase, String fecha_nac) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.estado = estado;
        this.clave = clave;
        this.firebase = firebase;
        this.fecha_nac = fecha_nac;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFirebase() {
        return firebase;
    }

    public void setFirebase(String firebase) {
        this.firebase = firebase;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }
}
