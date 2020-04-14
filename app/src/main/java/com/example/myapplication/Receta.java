package com.example.myapplication;

import android.media.Image;

public class Receta {
    private String nombre;
    private Image imagen;
    private String[] healthLabels;
    private String[] ingredientes;
    private String calorias;
    private String tiempo;

    public Receta(){
        this.nombre = "";
        /*this.imagen = null;
        this.healthLabels = null;
        this.ingredientes = null;
        this.calorias = "";
        this.tiempo = "";*/
    }

    public void setName(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
}