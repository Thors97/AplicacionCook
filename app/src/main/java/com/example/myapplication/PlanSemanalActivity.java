package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PlanSemanalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_semanal);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    //Acciones cuando clickamos algun item (Setting/About o del NavBar)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

        // Se comprueba cuál de las dos posibles opciones es, settings o about
        switch (id){
            case R.id.action_settings:
                System.out.println("APPMOV: About action...");
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentSettings = new Intent(this, UsuarioActivity.class);

                // Iniciamos la nueva actividad
                startActivity(intentSettings);
            break;

                case R.id.action_about:
                System.out.println("APPMOV: About action...");
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentAbout = new Intent(this, AppMovilActivity.class);

                // Iniciamos la nueva actividad
                startActivity(intentAbout);
            break;
            //NavBar
                case R.id.action_favorite:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentFav = new Intent(this,FavoritosActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentFav);

                System.out.println("APPMOV: About action...");

            break;
                case R.id.action_fridge:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentFridge = new Intent(this,NeveraActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentFridge);

                System.out.println("APPMOV: About action...");

            break;
            case R.id.action_user:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentUser = new Intent(this,PerfilActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentUser);

                System.out.println("APPMOV: About action...");

            break;
            case R.id.action_search:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentSearch = new Intent(this,BuscarRecetasActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentSearch);

                System.out.println("APPMOV: About action...");

            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
