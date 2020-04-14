package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UsuarioActivity extends AppCompatActivity {
    private ArrayList<String> itemIntolerancias;
    private ArrayAdapter<String> adapterIntolerancias;
    private ArrayList<String> itemIngredientesNo;
    private ArrayAdapter<String> adapterIngredientesNo;

    private ImageView fotoPerfil;

    private EditText nombreApellidos;
    private TextView edad;
    private Button establecerBtn;
    private EditText peso;
    private EditText altura;
    private ImageButton btnAddIntolerancias;
    private GridView listaIntolerancias;
    private EditText editItemIntolerancias;
    private ImageButton btnAddIngredientes;
    private GridView listaIngredientesNo;
    private EditText editItemIngredientes;

    Calendar c;
    DatePickerDialog fecha;

    public int editar=0; //Variable que en función de su valor nos permite editar o no la Activity

    // código de acción para lanzar un Intent que solicite una captura
    private static final int CODIGO_HACER_FOTO = 100;

    // ubicación de la imagen tomada
    private File ubicacion = null;

    // Clave para no perder la ruta en caso de destrucción de la activity
    private final static String CLAVE_RUTA_IMAGEN = "CLAVE_RUTA_IMAGEN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  if (editar==0) { //si pongo aquí 1 no funciona
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        //PREFERENCIAS
        //5_Android_Prefences_Fichero_BD.pdf
        //        // Recuperamos la informacion salvada en la preferencia
        //        String sharedPrefFile = "com.uc3m.it.preferencesappmovprefs";
        //        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        //        String name = mPreferences.getString("NAME", "No name defined yet!");
        //        No se donde recuperar las preferencias para que si doy para atrás y luego hacia delante sigan estando
        //        los datos que he introducido

        //Spinner para el sexo de la persona
        //2_Android_InterfacesUsuario_v2.pdf UC3M (págs 18, 19)
        final String[] optionsSex = new String[]{"Mujer", "Hombre"};
        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsSex);

        final Spinner sexSpinner = (Spinner) findViewById(R.id.sexo);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//Layout de la lista al desplegarse para seleccionar opción
        sexSpinner.setAdapter(adapterSex);

        //instanciamos
        fotoPerfil = (ImageView) findViewById(R.id.fotoUsuario);
        nombreApellidos = (EditText) findViewById(R.id.nombreApellidos);
        edad = (TextView) findViewById(R.id.edadEdit);
        establecerBtn = (Button) findViewById(R.id.addBirthayBtn);
        peso = (EditText) findViewById(R.id.pesoEdit);
        altura = (EditText) findViewById(R.id.alturaEdit);
        btnAddIntolerancias = (ImageButton) findViewById(R.id.addIntoleranciasBtn);
        listaIntolerancias = (GridView) findViewById(R.id.intoleranciasLV);
        editItemIntolerancias = (EditText) findViewById(R.id.intoleranciasEdit);
        btnAddIngredientes = (ImageButton) findViewById(R.id.addIngredientesBtn);
        listaIngredientesNo = (GridView) findViewById(R.id.ingredientesLV);
        editItemIngredientes = (EditText) findViewById(R.id.ingredientesNoEdit);

        //Nos creamos un arrayList para el manejo de las listas
        itemIntolerancias = new ArrayList<>();
        itemIngredientesNo = new ArrayList<>();

        adapterIntolerancias = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemIntolerancias
        );
        adapterIngredientesNo = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemIngredientesNo
        );

        // OnClickListener
        btnAddIntolerancias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemIntolerancia();
            }
        });

        btnAddIngredientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemIngredientes();
            }
        });

        //DatePickerDialog
        //2_Android_InterfacesUsuario_v2.pdf UC3M
        //https://www.youtube.com/watch?v=-mJmScTAWyQ
        establecerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int año = c.get(Calendar.YEAR);

                fecha = new DatePickerDialog(UsuarioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edad.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, dia, mes, año);
                fecha.show();
            }
        });
        listaIntolerancias.setAdapter(adapterIntolerancias);//establece los datos detrás del ListView
        listaIngredientesNo.setAdapter(adapterIngredientesNo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // }//end if
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

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
            Intent intent = new Intent(this,NeveraActivity.class);
            // Iniciamos la nueva actividad
            startActivity(intent);

            System.out.println("APPMOV: About action...");
            return true;
        }
        if (id == R.id.action_user) {
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

        if (id == R.id.action_edit) {
            //editar=1;//Podemos editar el perfil de usuario
            System.out.println("APPMOV: About action...");
            return true;
        }

        if (id == R.id.action_save) {
            System.out.println("APPMOV: About action...");
            savePref();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void savePref() {
        // Creamos colecciÃ³n de preferencias
        String sharedPrefFile = "com.uc3m.it.preferencesappmovprefs";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Obtenemos un editor de preferencias
        SharedPreferences.Editor editor = mPreferences.edit();

        // Guardamos el valor en una preferencia
        editor.putString("NAME", nombreApellidos.getText().toString());
        editor.putString("Nombre",nombreApellidos.getText().toString() );//insertamos los valores con su método put<type> correspondiente
        editor.putString("Edad",edad.getText().toString());
        editor.putString("Peso",peso.getText().toString());
        editor.putString("Intolerancias",editItemIntolerancias.getText().toString());
        editor.putString("Ingredientes",editItemIngredientes.getText().toString());
        editor.apply();

    }

    private void addItemIntolerancia() {
        String itemText = editItemIntolerancias.getText().toString();

        if (itemText.isEmpty()) {

            //AlertDialog para indicar al usuario que no ha introducido nada
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

            return;//si no hay nada metido, que no se añada
        }
        itemIntolerancias.add(itemText);
        adapterIntolerancias.notifyDataSetChanged();//Notifica a los observadores adjuntos que los datos subyacentes
        // han cambiado y que cualquier Vista que refleje el conjunto de
        // datos debería actualizarse.
    }
    private void addItemIngredientes() {
        String itemText = editItemIngredientes.getText().toString();

        if (itemText.isEmpty()) {

            //AlertDialog para indicar al usuario que no ha introducido nada
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
            return;//si no hay nada metido, que no se añada

        }

        itemIngredientesNo.add(itemText);
        adapterIngredientesNo.notifyDataSetChanged();//Notifica a los observadores adjuntos que los datos subyacentes
        // han cambiado y que cualquier Vista que refleje el conjunto de
        // datos debería actualizarse.
    }

    //Foto de perfil de usuario
    // Función que invoca un intent para capturar una foto
    //https://developer.android.com/training/camera/photobasics?hl=es-419
    /*
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Si llamamos a startActivityForResult() con un intent que ninguna app puede manejar, la app fallará
            // Por lo tanto, siempre que el resultado no sea nulo, se puede usar el intent
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    */
    //La aplicación de cámara de Android codifica la foto en el Intent de devolución que se entrega a onActivityResult()
    // como un Bitmap pequeño en la carpeta Extras, en los "data" clave. El siguiente código recupera esta imagen y la muestra en un ImageView

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoPerfil.setImageBitmap(imageBitmap);
        }
    }  */
    //manejo de la imagen de perfil de usuario
    // https://www.youtube.com/watch?v=jFYAp42rMEA

    // En caso de que la activity sea destruida mientras que otra app toma la fotografía,
    // es necesario guardar la ruta y restaurarla de nuevo, puesto que si no, no habrá forma
    // de acceder a la imagen.

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ubicacion != null) {
            savedInstanceState.putString(CLAVE_RUTA_IMAGEN, ubicacion.getPath());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CLAVE_RUTA_IMAGEN)) {
            ubicacion = new File(savedInstanceState.getString(CLAVE_RUTA_IMAGEN));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    public void onClick(View v) {
        // si el usuario pulsa el botón, pedimos una captura mediante un Intent
        // que solicite la acción de una app apropiada para la tarea
        if (v.getId() == R.id.button) {

            // primero comprobamos que existe alguna cámara (posterior o frontal) con la que poder
            // hacer la foto, ya que especificamos en el manifest que no era obligatorio tener cámara
            // para poder instalar la app:
            // <uses-feature android:name="android.hardware.camera" android:required="false" />
            PackageManager manager = getPackageManager();
            if (! manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                Toast.makeText(this, "No se dispone de cámara",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // solicitamos específicamente la captura de una imagen
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // creamos una ruta en la que guardar la imagen
            // y damos esta ubicación a la app que se encargue de hacerla
            ubicacion = obtenerUbicacionImagen(); // objeto File
            // nuestro método obtenerUbicacionImagen() puede devolver null si el
            // sistema de almacenamiento externo no está disponible, por lo que
            // dependemos de que no sea así para poder iniciar la captura
            if (ubicacion != null) {
                Uri uriImagen = Uri.fromFile(ubicacion);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);

                // IMPORTANTE: esperamos el resultado de la operación en onActivityResult()
                // para saber si se pudo hacer la foto o no y emprender acciones acordes
                startActivityForResult(intent, CODIGO_HACER_FOTO);
            } else {
                Toast.makeText(this, "No se puede acceder al sistema de archivos",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    /** Creamos un objeto File (que no es otra cosa que una ruta) para guardar ahí la foto */
    private File obtenerUbicacionImagen() {

        // ====>>> Se recomienda utilizar almacenamiento EXTERNO
        //         siempre que se guarden archivos MULTIMEDIA

        File directorioExterno;


        // IMPORTANTE: Primero comprobamos que el almacenamiento externo está disponible para escritura.
        // Ver posibles estados en:
        // http://developer.android.com/reference/android/os/Environment.html#getExternalStorageState()
        if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        // ====>>> Puedes consultar las distintas opciones de acceso a ficheros a través de la
        //         clase Environment:
        //         http://developer.android.com/reference/android/os/Environment.html

        // Usaremos el directorio PRIVADO de la aplicación ubicado en almacenamiento externo,
        // que se borra al desinstalarla
        directorioExterno = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Otra opción es utilizar un directorio PÚBLICO, también en almacenamiento externo,
        // que puede ser escaneado y accedido por otras apps,
        // y que persiste aun después de desinstalarla.
        // Para esto, comentar la línea de más arriba y descomentar las dos siguientes:
        //directorioExterno = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES), "HelloCamera");


        // Si aún no existe el directorio, lo creamos
        if (! directorioExterno.exists()){
            if (! directorioExterno.mkdirs()){
                Log.d("HelloCamera", "no se puede crear el directorio");
                return null;
            }
        }

        // Creamos un nombre de fichero dentro de ese directorio
        // (es buena idea utilizar un timestamp para evitar sobreescribir imágenes)
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File fichero = new File(directorioExterno.getPath() + File.separator +
                "IMG_"+ timestamp + ".jpg");

        // Devolvemos la ruta completa que hemos creado
        return fichero;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + (data == null ? "null" : "not null"));

        if (requestCode == CODIGO_HACER_FOTO) {
            if (resultCode != RESULT_CANCELED) {
                // La imagen ha sido capturada y grabada en la ubicación
                // que se señaló en el Intent

                // TextView miTexto = (TextView) findViewById(R.id.texto);
                // miTexto.setText(ubicacion.getPath());

                ImageView miImagen = (ImageView) findViewById(R.id.fotoUsuario);
                Bitmap miBitmap = BitmapFactory.decodeFile(ubicacion.getPath());
                miImagen.setImageBitmap(miBitmap);

                // NOTA: a pesar de los ejemplos acerca de este tema,
                // el Intent del resultado suele venir a NULL y hay que guardar
                // la URI en el momento de su creación, no a posteriori
                // if (data != null)
                // Log.i("onActivityResult", data.getData().toString());

            } else {
                // La captura ha sido cancelada por el usuario, ha fallado,
                // o la otra app se cerró inesperadamente
            }
        }
    }

}