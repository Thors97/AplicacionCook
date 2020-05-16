package com.example.cookplus;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.net.Uri;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ProgressDialog;


public class BuscarRecetasActivity extends AppCompatActivity {

    //Estas variables son los parámetros para autenticarnos en el servidor.
    final String APP_ID = "638c05ef";
    final String API_KEY = "131fcc82ee2adeadd0e6d8e2e1d29f99";

    private ListView m_listview;
    private EditText m_edittext;
    private String alimento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_recetas);

        // Creamos un listview que va a contener los resultados de la consulta
        m_listview = (ListView) findViewById(R.id.id_list_view);
        m_edittext = (EditText)  findViewById(R.id.buscadorReceta);

    }

    /**
     * Al pinchar sobre el boton de buscar ejecuta la búsqueda. Recupera el alimento escrito
     * si este existe y ejecuta la llamada al execute.
     *
     * @param view
     */
    public void buscar(View view){
        alimento = m_edittext.getText().toString();
        if (alimento.isEmpty()) {

            //AlertDialog
            // si no hay nada metido, no puede buscar nada
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Error");
            ad.setMessage(" No ha introducido nada ");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            ad.show();
            return;
        }
        new BuscarReceta().execute(alimento);
    }

    /**
     * Este método es el que controla al navegación del navbottom.
     * @param item
     * @return boolean.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

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
            System.out.println("APPMOV: About action...");
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    private class BuscarReceta extends AsyncTask<String , Object, ArrayList<Receta>> {


        protected ArrayList<Receta> doInBackground(String... urls) {

            ArrayList<Receta> temp;
            String alimento = urls[0];

            String url = "https://api.edamam.com/search?q=" + alimento + "&app_id=" + APP_ID + "&app_key=" + API_KEY;

            Set<String> salud, dieta;

            /* Accedemos a las preferencias que es donde tenemos guardados los datos
                sobre el tipo de dieta que queremos y sobre las alergias.
            */
            String sharedPrefFile = "com.example.cookplus";
            SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

            /* Comprobamos que las preferencias no estan vacías. Recuperamos los datos de los alimentos(salud) y
                del tipo de dieta que queremos para añadirselos a la consulta al servidor.
            * */
            if (mPreferences != null) {
                salud = mPreferences.getStringSet("INTOLERANCIAS", new HashSet<String>());

                dieta = mPreferences.getStringSet("INGREDIENTES", new HashSet<String>());

                if (salud.size() > 0) {
                    url = url + "&" + tipoSalud(salud);
                }
                if (dieta.size() > 0) {
                    url = url + "&" + tipoDieta(dieta);
                }
            }

            temp = makeCall(url);
            return temp;
        }

        /**
         *  Esta función se utiliza para convertir las preferencias al formato del servidor y poder hacer la
         *  consulta correctamente.
         * @param ingredientes
         * @return String Devuelve los diferentes ingredientes en el formato que acepta el servidor.
         */

        protected String tipoSalud(Set<String> ingredientes) {
            String health = "health=";
            int count = 0;
            for (String ingr : ingredientes) {
                if (ingr.equals("Sin azucar")) {
                    ingr = "sugar-conscious";
                } else if (ingr.equals("Cacahuetes")) {
                    ingr = "peanut-free";
                } else if (ingr.equals("Alcohol")) {
                    ingr = "alcohol-free";
                } else if (ingr.equals("Frutos Secos")) {
                    ingr = "tree-nut-free";
                } else if (ingr.equals("Vegana")) {
                    ingr = "vegan";
                } else if (ingr.equals("Vegetariana")) {
                    ingr = "vegetarian";
                }

                if (count == ingredientes.size() - 1)
                    health = health + ingr;
                else
                    health = health + ingr + "&health=";
                count++;

            }

            return health;
        }

        /**
         * Esta función es la encargada de convertir el tipo de dieta a el formato del servidor.
         * @param dieta
         * @return Convierte el tipo de dieta guardado en las preferencias al tipo de parametro del servidor.
         */

        protected String tipoDieta(Set<String> dieta) {
            String health = "diet=";

            int count = 0;
            for (String ingrediente : dieta) {
                if (ingrediente.equals("Balanceada")) {
                    ingrediente = "balanced";
                } else if (ingrediente.equals("Alta en proteína")) {
                    ingrediente = "high-protein";
                } else if (ingrediente.equals("Baja en grasa")) {
                    ingrediente = "low-fat";
                } else if (ingrediente.equals("Baja en carbohidratos")) {
                    ingrediente = "low-carb";
                }

                if (count == dieta.size() - 1)
                    health = health + ingrediente;
                else
                    health = health + ingrediente + "&diet=";
                count++;
            }

            return health;
        }


        //Progress dialog para notificar que se están buscando recetas
        ProgressDialog progress_dialog = new ProgressDialog(BuscarRecetasActivity.this);
        @Override
        protected void onPreExecute() {
            //Mostramos el progressDialog en la activity
            progress_dialog.setTitle("Espere por favor");
            progress_dialog.setMessage("   Buscando recetas...");
            progress_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_dialog.show();
        }

        /**
         * Este método es el encargado de actualizar el XML y añadir las recetas buscadas.
         * @param result
         */
        protected void onPostExecute(ArrayList<Receta> result) {


            final BuscarRecetasAdapter adapter = new BuscarRecetasAdapter(BuscarRecetasActivity.this, R.layout.buscar_recetas, result);
            m_listview.setAdapter(adapter);

            /*
             *  Código que permite que al pulsar en cada rectea está se abra en el navegador.
             *  Recurso: Para implementar este código hemos utilizado de referencia el siguiente codigo de StackOverflow
             *  https://stackoverflow.com/questions/36810210/listview-custom-adapter-onclick-launch-url-link
             */
            m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String linked = adapter.getLink(position);
                    Uri url = Uri.parse(linked);
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);

                }
            });
            //Una vez hemos realizado la busqueda cerramos el cuadro de progreso.
            progress_dialog.dismiss();

        }
    }

    /**
     * Ejecuta la consulta contra el servidor y parsea los datos recibidos.
     * Los datos se reciben en un JSON.
     * @param stringURL
     * @return ArrayList que contiene las recetas recuperadas del JSON.
     */
    public static ArrayList<Receta> makeCall(String stringURL) {

        URL url = null;
        BufferedInputStream is = null;
        JsonReader jsonReader;
        ArrayList<Receta> temp = new ArrayList<Receta>();

        try {
            url = new URL(stringURL);
        } catch (Exception ex) {
            System.out.println("Malformed URL");
        }

        try {
            if (url != null) {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                System.out.println(urlConnection);
                is = new BufferedInputStream(urlConnection.getInputStream());
            }
        } catch (IOException ioe) {
            System.out.println("IOException");
        }

        if (is != null) {
            try {
                jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                jsonReader.beginObject();
                Receta receta;

                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    //Buscamos la cadena hits
                    if(name.equals("hits")){
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            receta = new Receta();
                            jsonReader.beginObject();
                            //comienza un objeto
                            while (jsonReader.hasNext()) {
                                name = jsonReader.nextName();
                                if (name.equals("recipe")) {
                                    jsonReader.beginObject();
                                    while(jsonReader.hasNext()){
                                        name = jsonReader.nextName();
                                        if (name.equals("label")) {
                                            receta.setName(jsonReader.nextString());
                                        } else if (name.equals("image")) {
                                            receta.setImagen(getBitmapFromURL(jsonReader.nextString()));
                                        } else if (name.equals("url")) {
                                            receta.setUrl(jsonReader.nextString());
                                        } else if (name.equals("ingredients")) {
                                            jsonReader.beginArray();
                                            while (jsonReader.hasNext()) {
                                                ArrayList<String> ingredientes = new ArrayList<String>();
                                                jsonReader.beginObject();
                                                while (jsonReader.hasNext()) {
                                                    name = jsonReader.nextName();
                                                    if (name.equals("text")) {
                                                        ingredientes.add(jsonReader.nextString());
                                                    } else {
                                                        jsonReader.skipValue();
                                                    }
                                                }
                                                receta.setIngredientes(ingredientes);
                                                jsonReader.endObject();
                                            }
                                            jsonReader.endArray();
                                        } else if (name.equals("calories")) {
                                            receta.setCalorias(jsonReader.nextString());
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                }else{
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            temp.add(receta);
                        }
                        jsonReader.endArray();
                    }else{
                        jsonReader.skipValue();
                    }
                }

            } catch (Exception e) {
                System.out.println("Exception");
                return new ArrayList<Receta>();
            }
        }
        return temp;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Nos permite descargarnos la imagen desde una URL concreta convirtiendola a BITMAP
     * y posteriormente añadirla a la receta en la llamada.
     * Para implementar este código nos hemos ayudado de una entrada de StackOverflow.
     * Página: https://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android
     ***/
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
