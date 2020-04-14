package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

        // Se comprueba cuál de las dos posibles opciones es, settings o about

        if (id == R.id.action_settings) {
            System.out.println("APPMOV: About action...");
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this, UsuarioActivity.class);

            // Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {
            System.out.println("APPMOV: About action...");
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this, AppMovilActivity.class);

            // Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }

        //NavBar
        if (id == R.id.action_favorite) {
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this,FavoritosActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }
        if (id == R.id.action_fridge) {
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this, NeveraActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }
        if (id == R.id.action_user) {
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this,PerfilActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }

        if (id == R.id.action_search) {
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this,BuscarRecetasActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void ensaladaGarbanzosButton(View view) {
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, RecetaActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void añadirMenuButton(View view) {
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, PlanSemanalActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void perfilUsuarioButton(View view){
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, UsuarioActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void perfilButton(View view){
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, PerfilActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void neveraButton(View view){
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, NeveraActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }
}