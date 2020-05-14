package com.example.myapplication;

import android.graphics.Bitmap;
import android.media.Image;

public class Receta {
    private String nombre;
    private Bitmap imagen;


    private String url;
    private String[] healthLabels;
    private String[] ingredientes;
    private String calorias;
    private String tiempo;


    public Receta(){
        this.nombre = "";
        this.imagen = null;
        this.ingredientes = null;
        this.calorias = "";
        this.url = url;


        /*  this.healthLabels = null;
        this.tiempo = "";*/
    }


    public void setName(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String[] getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String[] ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}