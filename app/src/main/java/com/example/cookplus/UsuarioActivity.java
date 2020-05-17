package com.example.cookplus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.text.SimpleDateFormat;

/**
 * Esta clase es la encargada de mostrar los datos del usuario.
 * En ella se guardan los diferentes datos mediante preferencias.
 */

public class UsuarioActivity extends AppCompatActivity {

    private ImageView fotoPerfil;
    private EditText nombreApellidos;
    private TextView edad;
    private Button establecerBtn;
    private EditText peso;
    private EditText altura;
    private TextView paramSaludablesEdit;
    private TextView dietaEdit;
    private Set<String> dieta, parametros;
    Calendar c;
    DatePickerDialog fecha;
    Spinner sexSpinner;

    // Código de acción para lanzar un Intent que solicite una captura
    private static final int CODIGO_HACER_FOTO = 100;
    // Variable que guarda la ruta de la imagen
    private File ubicacion = null;
    // Clave para no perder la ruta en caso de destrucción de la activity
    private final static String CLAVE_RUTA_IMAGEN = "CLAVE_RUTA_IMAGEN";
    private final static String NOMBRE = "NOMBRE";
    private final static String IMG = "IMG";
    private final static String SEXO = "SEXO";
    private final static String PESO = "PESO";
    private final static String ALTURA= "ALTURA";
    private final static String EDAD= "EDAD";
    private final static String PARAMSALU = "PARAMSALU";
    private final static String DIETA = "DIETA";

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        // SPINNER para el sexo de la persona
        // 2_Android_InterfacesUsuario_v2.pdf
        final String[] optionsSex = new String[]{"Mujer", "Hombre"};
        sexSpinner = (Spinner) findViewById(R.id.sexo);

        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsSex);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//Layout de la lista al desplegarse para seleccionar opción
        sexSpinner.setAdapter(adapterSex);

        // PREFERENCIAS
        // 5_Android_Prefences_Fichero_BD.pdf
        // Recuperamos la informacion salvada en la preferencia
        String sharedPrefFile = "com.example.myapplication";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String nombre, edadP, alturaP, pesoP, strImagen, sex_spinner;
        Set<String> dietaSet, paramSaluSet;

        if(mPreferences!=null){
            nombre= mPreferences.getString(NOMBRE, "");
            nombreApellidos = (EditText) findViewById(R.id.nombreApellidos);
            nombreApellidos.setText(nombre);

            strImagen = mPreferences.getString(IMG, "");

            byte[] decodedByte = Base64.decode(strImagen, 0);
            if (decodedByte != null) {
                Bitmap imagen = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

                fotoPerfil = (ImageView) findViewById(R.id.fotoUsuario);
                fotoPerfil.setImageBitmap(imagen);
            }

            int position = mPreferences.getInt(SEXO, -1);
            sexSpinner.setSelection(position);

            edadP= mPreferences.getString(EDAD, "");
            edad = (TextView) findViewById(R.id.edadEdit);
            edad.setText(edadP);

            alturaP= mPreferences.getString(ALTURA, "");
            altura = (EditText) findViewById(R.id.alturaEdit);
            altura.setText(alturaP);

            pesoP= mPreferences.getString(PESO, "");
            peso = (EditText) findViewById(R.id.pesoEdit);
            peso.setText(pesoP);

            paramSaluSet = mPreferences.getStringSet(PARAMSALU, new HashSet<String>());
            paramSaludablesEdit = (TextView) findViewById(R.id.paramSaludablesEdit);
            String parametrosSaludablesPintar = "";
            for (String ingrediente : paramSaluSet) {


                parametrosSaludablesPintar = parametrosSaludablesPintar + "\n" + ingrediente;
            }
            paramSaludablesEdit.setText(parametrosSaludablesPintar);


            dietaSet = mPreferences.getStringSet(DIETA, new HashSet<String>());
            dietaEdit = (TextView) findViewById(R.id.dietaEdit);
            String dietaPintar = "";
            for (String ingrediente : dietaSet) {

                dietaPintar = dietaPintar + "\n" + ingrediente;
            }

            dietaEdit.setText(dietaPintar);

        }

        // instanciamos
        establecerBtn = (Button) findViewById(R.id.addBirthayBtn);

        // DatePickerDialog
        // 2_Android_InterfacesUsuario_v2.pdf UC3M
        // Establecemos la fecha de nacimiento del usuario a través de un DatePickerDialog
        establecerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int año = c.get(Calendar.YEAR);

                fecha = new DatePickerDialog(UsuarioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int año, int mes, int dia) {
                        edad.setText(dia + "/" + (mes + 1) + "/" + año);
                    }
                }, dia, mes, año);
                fecha.show();
            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    /**
     *
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

        //NavBar
        switch (id){
            case R.id.action_favorite:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentFav = new Intent(this,FavoritosActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentFav);
                break;
            case R.id.action_fridge:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intentFridge = new Intent(this,NeveraActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intentFridge);
                break;
            case R.id.action_user:

                break;
            case R.id.action_search:
                // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
                Intent intent = new Intent(this,BuscarRecetasActivity.class);
                // Iniciamos la nueva actividad
                startActivity(intent);
                break;


            case R.id.action_save:
                // Guardamos los datos en preferencias
                savePref();
                //4_Android_Intents_BroadcastReceivers_Dialog.pdf UC3M
                // Toast para indicar al usuario que se han guardado los datos
                Context context = getApplicationContext();
                String msg = "Guardado";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para guardar los datos de perfil en preferencias
     */
    public void savePref() {

        // Creamos coleccion de preferencias
        String sharedPrefFile = "com.example.myapplication";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Obtenemos un editor de preferencias
        SharedPreferences.Editor editor = mPreferences.edit();

        // Guardamos el nombre en una preferencia
        editor.putString(NOMBRE,nombreApellidos.getText().toString() );//insertamos los valores con su método put<type> correspondiente

        // Obtenemos la imagen como mapa de bits a través del método getDrawingCache() y la guardamos
        fotoPerfil.buildDrawingCache();
        Bitmap bitmap = fotoPerfil.getDrawingCache();

        editor.putString(IMG, encodeTobase64(bitmap));

        // Guardamos la edad en una preferencia
        editor.putString(EDAD, edad.getText().toString());
        int posicion = sexSpinner.getSelectedItemPosition();


        // Guardamos el sexo en una preferencia
        editor.putInt(SEXO, posicion);
        editor.putString(PESO,peso.getText().toString());
        editor.putString(ALTURA,altura.getText().toString());

        // Guardamos los parametros saludables y el tipo de dieta en una preferencia
        editor.putStringSet(PARAMSALU, parametros);
        editor.putStringSet(DIETA, dieta);
        editor.putBoolean("PRIMERAVEZ", true);
        editor.apply();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ubicacion != null) {
            savedInstanceState.putString(CLAVE_RUTA_IMAGEN, ubicacion.getPath());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CLAVE_RUTA_IMAGEN)) {
            ubicacion = new File(savedInstanceState.getString(CLAVE_RUTA_IMAGEN));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Esta función es la encargada de iniciar la camara para tomar la foto.
     * @param v
     */
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            // Se comprueba que hay una camara disponible en el dispositivo.

            PackageManager manager = getPackageManager();
            if (! manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                Toast.makeText(this, "No se dispone de cámara",
                        Toast.LENGTH_LONG).show();
                return;
            }
            // Solicitamos la captura de una imagen
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /* Creamos una ruta en la que guardar la imagen
             y damos esta ubicación a la app que se encargue de hacerla*/
            ubicacion = obtenerUbicacionImagen();
            /*  obtenerUbicacionImagen() puede retornar null. Por eso lo controlamos.
                Devuelve null si el almacenamiento externo no esta disponible.*/

            if (ubicacion != null) {
                Uri uriImagen = Uri.fromFile(ubicacion);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);
                /* IMPORTANTE: esperamos el resultado de la operación en onActivityResult()
                 para saber si se pudo hacer la foto o no y emprender acciones acordes. */
                startActivityForResult(intent, CODIGO_HACER_FOTO);
            } else {
                Toast.makeText(this, "No se puede acceder al sistema de archivos",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Creamos un objeto File (que no es otra cosa que una ruta) para guardar ahí la foto
     * @return Devuelve la ruta en la que se va a guardar la imagen. Devuelve null si el medio externo no esta disponible.
     */
    private File obtenerUbicacionImagen() {

        File directorioExterno;

        /*
         IMPORTANTE: Primero comprobamos que el almacenamiento externo está disponible para escritura.
       */
        if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        /* Usaremos el directorio PRIVADO de la aplicación ubicado en almacenamiento externo,
         que se borra al desinstalarla*/

        directorioExterno = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        // Si aún no existe el directorio, lo creamos
        if (! directorioExterno.exists()){
            if (! directorioExterno.mkdirs()){
                Log.d("HelloCamera", "no se puede crear el directorio");
                return null;
            }
        }
        // Creamos un nombre de fichero dentro de ese directorio. Le añadimos el timestamp para no sobrescribir imagenes.
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File fichero = new File(directorioExterno.getPath() + File.separator +
                "IMG_"+ timestamp + ".jpg");
        // Devolvemos la ruta completa que hemos creado
        return fichero;
    }

    /**
     *  Este metodo nos permite codificar la imagen de bitmap a base64.
     *  Para implementar esta funcionalidad hemos hecho uso de este recurso:
     *  https://stackoverflow.com/questions/18072448/how-to-save-image-in-shared-preference-in-android-shared-preference-issue-in-a
     * @param image
     * @return String Imagen codificada a base64
     */
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    @Override
    /**
     *
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + (data == null ? "null" : "not null"));

        if (requestCode == CODIGO_HACER_FOTO) {
            if (resultCode != RESULT_CANCELED) {
                // La imagen ha sido capturada y grabada en la ubicación que se señaló en el Intent
                ImageView miImagen = (ImageView) findViewById(R.id.fotoUsuario);
                Bitmap miBitmap = BitmapFactory.decodeFile(ubicacion.getPath());
                miImagen.setImageBitmap(miBitmap);
            } else {
                /* Codigo a implementar si la captura se cancela por el usuario o la aplicacion falla */
            }
        }
    }

    /**
     * Funcion que nos permite crear un cuadro de diálogo donde se muestran los diferentes
     * parametros a seleccionar. Una vez seleccionados estos se guardan en un HasSet que
     * para posteiormente guardarlos en las preferencias. A su vez muestra los elementos seleccionados en la interfaz.
     * Para el desarrollo de funcionalidad nos hemos basado en el siguiente recurso.
     * Paágina: https://android--examples.blogspot.com/2015/04/alertdialog-in-android.html
     * @param  v
     */
    public void onTextViewClickedParametrosSaludables(View v) {


        final TextView tv = (TextView) findViewById(R.id.paramSaludablesEdit);
        tv.setText(null);
        // Creamos el cuadro de alerta.
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Seleccione los parametros saludables:");
        // Son los diferentes elementos que se van a mostrar en el cuadro de dialogo.
        final String[] ParametrosSaludables = new String[]{
                "Sin azúcar", "Cacahuetes", "Frutos Secos", "Alcohol", "Vegana", "Vegetariana"
        };

        //ArrayList en el que guardamos las posiciones de los items seleccionados en el cuadro de dialogo.
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        //Array donde guardamos si los items han sido selecionados o no.
        final boolean[] preCheckedItems = new boolean[]{
                false,false,false,false,false,false
        };

        adb.setMultiChoiceItems(ParametrosSaludables, preCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked){
                if(isChecked) {
                    selectedItems.add(which);
                } else if(selectedItems.contains(which)) {
                    selectedItems.remove(Integer.valueOf(which));
                }
            }
        });

        //Define los botones de OK
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                /*Hacemos un bucle para recorrer los elementos selecionados
                    cuando el usuario pulse el boton de OK.
                 */
                parametros = new HashSet<>();
                String paramSelected = "";
                for(int i = 0; i<selectedItems.size(); i++){

                    int IndexOfParam = selectedItems.get(i);


                    paramSelected = Arrays.asList(ParametrosSaludables).get(IndexOfParam);

                    parametros.add(paramSelected);
                    tv.setText(tv.getText() + paramSelected + "\n");

                }
            }
        });

        //Definimos el boton de Cancelar
        adb.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.show();
    }

    /**
     * Para el desarrollo de funcionalidad nos hemos basado en el siguiente recurso.
     * Página: https://android--examples.blogspot.com/2015/04/alertdialog-in-android.html
     * @param v
     */
    public void onTextViewClickedDieta(View v) {

        dieta = null;
        final TextView tv = (TextView) findViewById(R.id.dietaEdit);
        tv.setText(null);

        // Creamos el cuadro de alerta.
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Tipo de dieta:");

        final String[] Dieta = new String[]{
                "Balanceada", "Alta en proteína", "Baja en grasa", "Baja en carbohidratos"
        };

        //ArrayList en el que guardamos las posiciones de los items seleccionados en el cuadro de dialogo.
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        /* En este caso tiene que ser un radio button porque solo podemos selecionar un tipo de dieta */
        adb.setSingleChoiceItems(Dieta, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedItems.add(i);
            }
        });

        //Define los botones de OK
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                /*Hacemos un bucle para recorrer los elementos selecionados
                    cuando el usuario pulse el boton de OK.
                 */
                dieta = new HashSet<>();
                String dietaSelected = "";
                for(int i = 0; i<selectedItems.size(); i++){
                    int IndexOfDieta = selectedItems.get(i);

                    dietaSelected = Arrays.asList(Dieta).get(IndexOfDieta);
                    //Guardamos en el hasSet el texto de los items seleccionados.
                    dieta.add(dietaSelected);
                    //Mostramos el texto de los items seleccionados.
                    tv.setText(tv.getText() + dietaSelected + "\n");

                }
            }
        });

        //Definimos el boton de cancelar.
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        tv.setText(null);
        adb.show();
    }
}