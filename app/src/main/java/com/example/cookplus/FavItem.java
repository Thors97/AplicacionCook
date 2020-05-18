package com.example.cookplus;

/**
 * Esta clase es un JavaBean para modelar los datos de un item de la lista
 * de recetas favoritas.
 * Tiene un constructor y los getters y setters.
 */

class FavItem {
       private String titulo;

    public FavItem(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
