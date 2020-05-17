package com.example.cookplus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PrincipalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sharedPrefFile = "com.example.cookplus";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        /*
        Aquí comprobamos si es la primera vez que se ha accedido a la app
        o no. Si es la primera vez se lanza la pantalla de usuario, si no se carga la pantalla
        principal.
         */
        boolean firstTime = mPreferences.getBoolean("PRIMERAVEZ", false);
        if (!firstTime) {
            Intent intentSettings = new Intent(this, UsuarioActivity.class);
            startActivity(intentSettings);
        } else if (firstTime) {
            setContentView(R.layout.principal);
        }

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
                Intent intentFridge = new Intent(this, NeveraActivity.class);
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

    public void goBuscador(View view) {
        // Creamos el Intent que va a lanzar la segunda activity
        Intent intent = new Intent(this, BuscarRecetasActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void añadirMenuButton(View view) {
        // Creamos el Intent que va a lanzar la segunda activity
        Intent intent = new Intent(this, PlanSemanalActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void perfilButton(View view){
        // Creamos el Intent que va a lanzar la segunda activity
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
