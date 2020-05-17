package com.example.cookplus;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.Intent;

public class NeveraActivity extends AppCompatActivity {

    private ArrayList<NeveraItem> despensaAL;
    private ArrayList<NeveraItem> listaAL;

    private NeveraAdapter adapterDespensa;
    private NeveraAdapter adapterLista;

    private ListView despensa;
    private ListView lista;

    private ImageButton btnAddDespensa;
    private ImageButton btnAddLista;

    private EditText editItemDespensa;
    private EditText editItemLista;

    private AlertDialog.Builder ad;

    private DbAdapter dbAdapter;

    private static final String DATABASE_TABLE_LISTA = "lista";
    private static final String DATABASE_TABLE_DESPENSA = "despensa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nevera);

        ad = new AlertDialog.Builder(this);

        despensa = (ListView) findViewById(R.id.despensaLV);
        lista = (ListView) findViewById(R.id.compraLV);

        btnAddDespensa = (ImageButton) findViewById(R.id.addDespensaBtn);
        btnAddLista = (ImageButton) findViewById(R.id.addListaBtn);

        editItemDespensa= (EditText) findViewById(R.id.anadirDespensaET);
        editItemLista = (EditText) findViewById(R.id.anadirListaET);

        despensaAL = new ArrayList<>();
        listaAL = new ArrayList<>();

        //Crea el adaptador de la BD y la abre
        dbAdapter = new DbAdapter(this);
        dbAdapter.open();

        //Crea los adapatadores para las listas
        adapterDespensa = new NeveraAdapter(
                this,
                R.layout.nevera_item,
                despensaAL
        );

        adapterLista  = new NeveraAdapter(
                this,
                R.layout.nevera_item,
                listaAL
        );

        //Añade un item a lista correspondiente cuando se pulsa el boton (+)
        btnAddDespensa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(DATABASE_TABLE_DESPENSA, despensa, despensaAL, adapterDespensa, editItemDespensa);
            }
        });

        btnAddLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(DATABASE_TABLE_LISTA, lista, listaAL, adapterLista, editItemLista);
            }
        });

        /*
         * Añade un item a lista correspondiente cuando se pulsa el boton DONE del teclado
         * Fuente: https://www.youtube.com/watch?v=742V81aJ75o
         */
        editItemDespensa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem(DATABASE_TABLE_DESPENSA, despensa, despensaAL, adapterDespensa, editItemDespensa);
                return true;
            }
        });

        editItemLista.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem(DATABASE_TABLE_LISTA, lista, listaAL, adapterLista, editItemLista);
                return true;
            }
        });

        //Borra un item con click prolongado de la lista correspondiente
        despensa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> lista, View item, int posicion, long id) {
                borrarItem(DATABASE_TABLE_DESPENSA, despensaAL, adapterDespensa, posicion, id);
                return true;
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> lista, View item, int posicion, long id) {
                borrarItem(DATABASE_TABLE_LISTA, listaAL, adapterLista, posicion, id);
                return true;
            }
        });

        /*
         * Cambia el aspecto del checkbox del item de la lista correspondiente al clickar
         * (marcar como comprado/agotado) y actualiza el valor del checked de ese elemento
         * en la tabla correspondiente de la base de datos.
         */
        despensa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int pos, long id) {
                //cambia aspecto del checkbox(click o unclick)
                despensaAL.get(pos).toggleChecked();
                adapterDespensa.notifyDataSetChanged();
                //recoge el par de datos (alimento y si está cheeckado ahora o no)
                boolean isChecked = despensaAL.get(pos).isChecked();
                String alimento = despensaAL.get(pos).getText();
                //convierte a int para la bbdd
                int checked = (isChecked)? 1:0; //0 = not checked (false) ; 1 = checked (true)
                //actualiza
                dbAdapter.updateItem(DATABASE_TABLE_DESPENSA, alimento, checked);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int pos, long id) {
                //cambia aspecto del checkbox(click o unclick)
                listaAL.get(pos).toggleChecked();
                adapterLista.notifyDataSetChanged();
                //recoge el par de datos (alimento y si está cheeckado ahora o no)
                boolean isChecked = listaAL.get(pos).isChecked();
                String alimento = listaAL.get(pos).getText();
                //convierte a int para la bbdd
                int checked = (isChecked)? 1:0; //0 = not checked (false) ; 1 = checked (true)
                //actualiza
                dbAdapter.updateItem(DATABASE_TABLE_LISTA, alimento, checked);
            }
        });

        despensa.setAdapter(adapterDespensa);
        lista.setAdapter(adapterLista);

        rellenarLista(DATABASE_TABLE_DESPENSA, despensaAL, adapterDespensa);
        rellenarLista(DATABASE_TABLE_LISTA, listaAL, adapterLista);
    }
    /**
     * Rellena la lista indicada con los datos de la tabla correspondiente de
     * la base de datos
     *
     * @param tabla
     * @param item
     * @param adapter
     * */
    private void rellenarLista(String tabla, ArrayList<NeveraItem> item, ArrayAdapter<NeveraItem> adapter) {
        //recoge todos los datos de la bbdd
        Cursor itemsCursor = dbAdapter.fetchAllItems(tabla);
        //si la bbdd no está vacía, rellenamos la lista con los alimentos hasta el final de la bbddd
        if(itemsCursor.moveToFirst()) {
            do {
                //recojemos el par de datos (nombre del alimento y si está o no checked)
                String alimento = itemsCursor.getString(itemsCursor.getColumnIndex("alimento")); //la 2a columna es la de alimentos
                int checked = itemsCursor.getInt(itemsCursor.getColumnIndex("checked")); // la 3a columna es la de checked
                //convertimos la info de check a int para la bbdd
                boolean isChecked;
                isChecked = (checked == 1); //0 = not checked (false) ; 1 = checked (true)
                //añadimos el par de dator a la lista
                item.add(new NeveraItem(alimento, isChecked));

            } while (itemsCursor.moveToNext());

            //notificamos al adapter de que ocurrio un cambio
            adapter.notifyDataSetChanged();
        }
    }



    //Método para añadir items al ListView de cada lista
    private void addItem(String tabla, ListView lista, ArrayList<NeveraItem> item, ArrayAdapter<NeveraItem> adapter, EditText editItem) {
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

            //añadimos elemento a la bbdd
            dbAdapter.createItem(tabla, itemText, 0);
            item.add(new NeveraItem(itemText));
            //notificamos al adapter de que ocurrio un cambio
            adapter.notifyDataSetChanged();
            //Elimina texto en el EditView para escribir el siguiente
            editItem.setText("");
            //scrollea la lista hasta el ultimo item
            lista.smoothScrollToPosition(item.size() - 1);

        }
    }

    //Metodos para borrar de los ListView de cada lista
    public void borrarItem(final String tabla, final ArrayList<NeveraItem> item, final ArrayAdapter<NeveraItem> adapter, final int posicion, final long id){
        //creamos alerta de borrado del alimento
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.eliminar);
        String msg = getResources().getString(R.string.mensajeEliminar);
        builder.setMessage(String.format(msg, item.get(posicion).getText()));
        builder.setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //recojemos el nombre del alimento a borrar
                String name = item.get(posicion).getText();
                //lo borramos de la bbdd
                dbAdapter.deleteItem(tabla, name);

                //borrado de la lista (del araylist)
                item.remove(posicion);
                adapter.notifyDataSetChanged();

                //mesaje toast que informa del borrado
                Context context = getApplicationContext();
                CharSequence text = "Borrado";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();


    }

    public void borrarChecked(String tabla, final ArrayList<NeveraItem> item, final ArrayAdapter<NeveraItem> adapter){
        ArrayList<NeveraItem> itemsChecked = new ArrayList<>();
        //recojo todos los datos de la bbdd
        Cursor itemsCursor = dbAdapter.fetchAllItems(tabla);
        if(itemsCursor.moveToFirst()){
            do {
                //recojemos el  nombre del alimento a borrar
                String alimento = itemsCursor.getString(itemsCursor.getColumnIndex("alimento")); //la 2a columna es la de alimentos
                int checked = itemsCursor.getInt(itemsCursor.getColumnIndex("checked")); // la 3a columna es la de checked
                boolean isChecked;
                isChecked = (checked == 1); //0 = not checked (false) ; 1 = checked (true)
                if (checked == 1) {
                    itemsChecked.add(new NeveraItem(alimento, isChecked));
                    //borro el alimento de la bbdd
                    dbAdapter.deleteItem(tabla, alimento);
                }

            } while (itemsCursor.moveToNext());
        }
        //borramos todos los elementos para actualizar la lista leyendo de la bbdd
        item.clear();
        rellenarLista(tabla, item, adapter);

        adapter.notifyDataSetChanged();

        //mesaje toast que informa del borrado
        Context context = getApplicationContext();
        CharSequence text = "Borrado";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void borrarTodo(final String tabla, final ArrayList<NeveraItem> item, final ArrayAdapter<NeveraItem> adapter){

        //creamos alerta de borrado del alimento
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.eliminar);
        String msg = getResources().getString(R.string.mensajeEliminarTodo);
        builder.setMessage(String.format(msg, tabla));
        builder.setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //borrar todos los elementos de la lista
                item.clear();
                adapter.notifyDataSetChanged();
                //borrar todos los elementos de la bbdd
                dbAdapter.clear(tabla);

                //mesaje toast que informa del borrado
                Context context = getApplicationContext();
                CharSequence text = "Borrado";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }


    //Menu de Settings y About
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_nevera, menu);

        return true;
    }

    //Acciones cuando clickamos algun item (Setting/About o del NavBar)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona la seleccion de opciones en el menú
        int id = item.getItemId();

        if (id == R.id.action_delete_despensa) {
            borrarChecked(DATABASE_TABLE_DESPENSA, despensaAL, adapterDespensa);
        }

        if (id == R.id.action_deleteAll_despensa) {
            borrarTodo(DATABASE_TABLE_DESPENSA, despensaAL, adapterDespensa);
        }

        if (id == R.id.action_delete_lista) {
            borrarChecked(DATABASE_TABLE_LISTA, listaAL, adapterLista);
        }

        if (id == R.id.action_deleteAll_lista) {
            borrarTodo(DATABASE_TABLE_LISTA, listaAL, adapterLista);
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

