package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import java.util.ArrayList;

public class NeveraActivity extends AppCompatActivity {

    private ArrayList<NeveraItem> itemListaCompra;
    private ArrayList<NeveraItem> itemDespensa;

    private NeveraAdapter adapterListaCompra;
    private NeveraAdapter adapterDespensa;

    private ListView listaCompra;
    private ListView despensa;

    private ImageButton btnAddListaCompra;
    private ImageButton btnAddDespensa;

    private EditText editItemListaCompra;
    private EditText editItemDespensa;

    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nevera);

        ad = new AlertDialog.Builder(this);

        listaCompra = (ListView) findViewById(R.id.compraLV);
        despensa = (ListView) findViewById(R.id.despensaLV);

        btnAddListaCompra = (ImageButton) findViewById(R.id.addListaBtn);
        btnAddDespensa = (ImageButton) findViewById(R.id.addDespensaBtn);

        editItemListaCompra = (EditText) findViewById(R.id.anadirListaET);
        editItemDespensa = (EditText) findViewById(R.id.anadirDespensaET);

        itemListaCompra = new ArrayList<>();
        itemDespensa = new ArrayList<>();

        itemListaCompra.add(new NeveraItem("Zanahoria"));
        itemListaCompra.add(new NeveraItem("Papel WC"));

        itemDespensa.add(new NeveraItem("Lentejas"));

        adapterListaCompra = new NeveraAdapter(
                this,
                R.layout.nevera_item,
                itemListaCompra
        );
        adapterDespensa = new NeveraAdapter(
                this,
                R.layout.nevera_item,
                itemDespensa
        );

        //Añadir item a lista correspondiente cuando se pulsa el boton (+)
        btnAddListaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(listaCompra, itemListaCompra, adapterListaCompra, editItemListaCompra);
            }
        });

        btnAddDespensa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(despensa, itemDespensa, adapterDespensa, editItemDespensa);
            }
        });

        //Añadir item a lista correspondiente cuando se pulsa el Done del teclado
        editItemListaCompra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem(listaCompra, itemListaCompra, adapterListaCompra, editItemListaCompra);
                return true;
            }
        });

        editItemDespensa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem(despensa, itemDespensa, adapterDespensa, editItemDespensa);
                return true;
            }
        });

        //Borrar item de la lista con click prolongado
        listaCompra.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> lista, View item, int posicion, long id) {
                borrarItem(itemListaCompra, adapterListaCompra, posicion);
                return true;
            }
        });

        despensa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> lista, View item, int posicion, long id) {
                borrarItem(itemDespensa, adapterDespensa, posicion);
                return true;
            }
        });

        //Cambiar aspecto item de la lista con click (marcar comprado/agotado)
        listaCompra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
                itemListaCompra.get(pos).toggleChecked();
                adapterListaCompra.notifyDataSetChanged();
            }
        });

        despensa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
                itemDespensa.get(pos).toggleChecked();
                adapterDespensa.notifyDataSetChanged();
            }
        });

        listaCompra.setAdapter(adapterListaCompra);
        despensa.setAdapter(adapterDespensa);
    }

// --------------- MIS METODOS ------------------------------------------------------------------------------------------
    //Método para añadir items al ListView de cada lista
    private void addItem(ListView lista, ArrayList<NeveraItem> item, ArrayAdapter<NeveraItem> adapter, EditText editItem) {
        //Cogemos texto de la editText y lo convertimos a String
        String itemText = editItem.getText().toString();
        //Si esta vacío, no añade y salta ventana de Error
        if (itemText.isEmpty()){
            ad.setTitle(R.string.error);
            ad.setMessage(R.string.alimento_vacio_adv);
            ad.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    });
            ad.show();
        }else{
            item.add(new NeveraItem(itemText));
            //notificamos al adapter de que ocurrio un cambio
            adapter.notifyDataSetChanged();
            //Elimina texto en el EditView para escribir el siguiente
            editItem.setText("");
            //scrollea la lista hasta el ultimo item
            lista.smoothScrollToPosition(item.size()-1);
        }
    }

    //Metodos para borrar de los ListView de cada lista
    public void borrarItem(final ArrayList<NeveraItem> item, final ArrayAdapter<NeveraItem> adapter, final int posicion){
        //creamos alerta de borrado de un elemento
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.eliminar);
        String msg = getResources().getString(R.string.mensajeEliminar);
        builder.setMessage(String.format(msg, item.get(posicion).getText()));
        builder.setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.remove(posicion);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }




    //Menu de Settings y About
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
            // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
            Intent intent = new Intent(this,FavoritosActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }
        if (id == R.id.action_fridge) {
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

}

