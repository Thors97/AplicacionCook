package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Esta clase es un JavaBean para modelar los datos de una receta.
 * Tiene un constructor y los getters y setters.
 */

public class Receta {
    private String nombre;
    private Bitmap imagen;


    private String url;
    private String[] healthLabels;
    private ArrayList<String> ingredientes;
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

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
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