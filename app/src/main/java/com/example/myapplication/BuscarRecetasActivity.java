package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.graphics.BitmapFactory;


import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.app.ProgressDialog;

import javax.net.ssl.HttpsURLConnection;



public class BuscarRecetasActivity extends AppCompatActivity {

    //APP ID y API KEY
    final String APP_ID = "3116a446";
    final String API_KEY ="a98641147b27d0ffe831f2be4da7e3cd";

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

            ArrayList<Receta> temp;
            String alimento = urls[0];
            // make Call to the url

            temp = makeCall("https://api.edamam.com/search?q=" + alimento + "&app_id=" + APP_ID + "&app_key=" + API_KEY);

            return temp;
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
            List<String> listTitle = new ArrayList<String>();
            List<Bitmap> listImage = new ArrayList<Bitmap>();

            System.out.println("TAMAÑO RESULT" + result.size());

            for (int i = 0; i < result.size(); i++) {
                // make a list of the venus that are loaded in the list.
                // show the name, the category and the city
                // listTitle.add(i, result.get(i).getNombre());
                //listImage.add(i, result.get(i).getImagen());
                System.out.println("RESULT NAME:  " + result.get(i).getNombre());
            }

            BuscarRecetasAdapter adapter = new BuscarRecetasAdapter(BuscarRecetasActivity.this, R.layout.buscar_recetas, result);

            m_listview.setAdapter(adapter);
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
                                        }else{
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
