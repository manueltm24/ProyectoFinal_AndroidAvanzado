package com.example.proyectofinal_androidavanzado.Clases;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {

    private  String id;
    private String email;
    private String password;
    private String idImagen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public Usuario() {
    }

    public Usuario(String id, String email, String password, String idImagen) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.idImagen = idImagen;
    }
}
