package com.example.cookplus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity {

    private ArrayList<FavItem> itemFav;

    private ImageButton btn;

    private FavAdapter adapterFav;

    private ListView listaFav;

    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        ad = new AlertDialog.Builder(this);

        listaFav = (ListView) findViewById(R.id.favLV);

        //btn = (ImageButton) findViewById(R.id.expand_button);

        itemFav = new ArrayList<>();

        itemFav.add(new FavItem("Ensalada de garbanzos"));
        itemFav.add(new FavItem("Tarta de queso sin lactosa"));

        adapterFav = new FavAdapter(
                this,
                R.layout.fav_item,
                itemFav
        );

        listaFav.setAdapter(adapterFav);
    }

    public void expandReceta(View view) {
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, RecetaActivity.class);
        // Iniciamos la nueva actividad
        startActivity(intent);
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
            Intent intent = new Intent(this, PerfilActivity.class);
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

}
