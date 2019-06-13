package com.example.proyectofinal_androidavanzado.Clases;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Proyecto {

    private String id;
    private String idUsuario;
    private String nombre;
    private String descripcion;
    private String fechaPublicacion;
//    private Ubicacion ubicacion;
    private String latitud;
    private String longitud;
    private boolean remoto;
    private double pagoHora;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public boolean isRemoto() {
        return remoto;
    }

    public void setRemoto(boolean remoto) {
        this.remoto = remoto;
    }

    public double getPagoHora() {
        return pagoHora;
    }

    public void setPagoHora(double pagoHora) {
        this.pagoHora = pagoHora;
    }

    public Proyecto() {
    }


    public Proyecto(String id, String idUsuario, String nombre, String descripcion, String fechaPublicacion, String latitud, String longitud, boolean remoto, double pagoHora) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.remoto = remoto;
        this.pagoHora = pagoHora;
    }
}
