package com.rosario.hp.espaciojahiel.Entidades;

public class espacioAmigo {

    private String id;
    private String nombre;
    private String pagina;
    private String face;
    private String destacado;
    private String estado;
    private String instagram;

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    private String mail;
    private String telefono;
    private String contacto;

    public espacioAmigo(String id, String nombre, String pagina, String face, String destacado, String estado, String mail, String telefono, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.pagina = pagina;
        this.face = face;
        this.destacado = destacado;
        this.estado = estado;
        this.mail = mail;
        this.telefono = telefono;
        this.contacto = contacto;
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

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getDestacado() {
        return destacado;
    }

    public void setDestacado(String destacado) {
        this.destacado = destacado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}
