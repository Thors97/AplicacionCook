package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.BitmapFactory;


import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.app.ProgressDialog;

import javax.net.ssl.HttpsURLConnection;



public class BuscarRecetasActivity extends AppCompatActivity {

    //APP ID y API KEY
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

    public void buscar(View view){
        alimento = m_edittext.getText().toString();
        if (alimento.isEmpty()) {

            //AlertDialog
            // si no hay nada metido, no puede buscar nada
            //4_Android_Intents_BroadcastReceivers_Dialog.pdf UC3M (pág 30)
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

    public void recetaButton(View view){
        //creamos el intent
        Intent intent = new Intent(this, RecetaActivity.class);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    private class BuscarReceta extends AsyncTask<String , Object, ArrayList<Receta>> {

        @Override
        protected ArrayList<Receta> doInBackground(String... urls) {
            String health, diet = "";
            ArrayList<Receta> temp;
            String alimento = urls[0];
            String intolerancias = "";
            Set<String> salud, dieta;
            //Queremos acceder a las preferencias para poder filtrar.
            String sharedPrefFile = "com.example.myapplication";
            SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            String[] arrayIngredientes, arrayDieta = null;

            String url = "https://api.edamam.com/search?q=" + alimento + "&app_id=" + APP_ID + "&app_key=" + API_KEY;

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


            // make Call to the url
            temp = makeCall(url);

            return temp;
        }

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
        protected void onPostExecute(ArrayList<Receta> result) {

            // Aquí se actualiza el interfaz de usuario


            final BuscarRecetasAdapter adapter = new BuscarRecetasAdapter(BuscarRecetasActivity.this, R.layout.buscar_recetas, result);

            m_listview.setAdapter(adapter);
            m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String linked = adapter.getLink(position);
                    Uri url = Uri.parse(linked);
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);

                }
            });

            //Ocultamos el progressDialog
            progress_dialog.dismiss();

        }


    }
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
     * Este código ha sido sacado de StackOverflow.
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
