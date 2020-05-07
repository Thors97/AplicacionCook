package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UsuarioActivity extends AppCompatActivity {

    private ImageView fotoPerfil;
    private EditText nombreApellidos;
    private TextView edad;
    private Button establecerBtn;
    private EditText peso;
    private EditText altura;
    private TextView intoleranciasEdit;
    private TextView ingredientesNoEdit;

    Calendar c;
    DatePickerDialog fecha;
    Spinner sexSpinner;

    // código de acción para lanzar un Intent que solicite una captura
    private static final int CODIGO_HACER_FOTO = 100;
    // ubicación de la imagen tomada
    private File ubicacion = null;
    // Clave para no perder la ruta en caso de destrucción de la activity
    private final static String CLAVE_RUTA_IMAGEN = "CLAVE_RUTA_IMAGEN";
    private final static String NOMBRE = "NOMBRE";
    private final static String IMG = "IMG";
    private final static String SEXO = "SEXO";
    private final static String PESO = "PESO";
    private final static String ALTURA= "ALTURA";
    private final static String EDAD= "EDAD";
    private final static String INTOLERANCIAS= "INTOLERANCIAS";
    private final static String INGREDIENTES= "INGREDIENTES";

    //-----------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        // SPINNER para el sexo de la persona
        // 2_Android_InterfacesUsuario_v2.pdf UC3M (págs 18, 19)
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

        String nombre, edadP, alturaP, pesoP, intoleranciasP, ingredientesP, sex_spinner;

        if(mPreferences!=null){
            nombre= mPreferences.getString(NOMBRE, "");
            nombreApellidos = (EditText) findViewById(R.id.nombreApellidos);
            nombreApellidos.setText(nombre);

            byte[] decodedByte = Base64.decode(IMG.getBytes(),Base64.DEFAULT );
            System.out.println("recuperamos: "+decodedByte);
            if (decodedByte != null) {
                Bitmap imagen = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                System.out.println("recuperamos imagen: "+imagen);
                fotoPerfil = (ImageView) findViewById(R.id.fotoUsuario);
                fotoPerfil.setImageBitmap(imagen);
            }

//            int position= mPreferences.getInt(SEXO,0);
//              sexSpinner.setSelection(position);

            edadP= mPreferences.getString(EDAD, "");
            edad = (TextView) findViewById(R.id.edadEdit);
            edad.setText(edadP);

            alturaP= mPreferences.getString(ALTURA, "");
            altura = (EditText) findViewById(R.id.alturaEdit);
            altura.setText(alturaP);

            pesoP= mPreferences.getString(PESO, "");
            peso = (EditText) findViewById(R.id.pesoEdit);
            peso.setText(pesoP);

            intoleranciasP= mPreferences.getString(INTOLERANCIAS, "");
            intoleranciasEdit = (TextView) findViewById(R.id.intoleranciasEdit);
            intoleranciasEdit.setText(intoleranciasP);

            ingredientesP= mPreferences.getString(INGREDIENTES, "");
            ingredientesNoEdit = (TextView) findViewById(R.id.ingredientesNoEdit);
            ingredientesNoEdit.setText(ingredientesP);

        }

        // instanciamos
        establecerBtn = (Button) findViewById(R.id.addBirthayBtn);
        // DatePickerDialog
        // 2_Android_InterfacesUsuario_v2.pdf UC3M
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
        // }//end if
    }
    //-----------------------------------------------------------------------//
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }
    //-----------------------------------------------------------------------//
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
            case R.id.action_edit:
                // editar=1;//Podemos editar el perfil de usuario
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

    //-----------------------------------------------------------------------//

    public void savePref() {
        // Creamos coleccion de preferencias
        String sharedPrefFile = "com.example.myapplication";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Obtenemos un editor de preferencias
        SharedPreferences.Editor editor = mPreferences.edit();

        // Guardamos el valor en una preferencia
        editor.putString(NOMBRE,nombreApellidos.getText().toString() );//insertamos los valores con su método put<type> correspondiente

        // Obtenemos la imagen como mapa de bits a través del método getDrawingCache()
        fotoPerfil.buildDrawingCache();
        Bitmap bitmap = fotoPerfil.getDrawingCache();

        editor.putString(IMG, encodeTobase64(bitmap));
        System.out.println("Imagen: "+encodeTobase64(bitmap));
        editor.putString(EDAD,edad.getText().toString());
        //int posicion = Integer.parseInt(sexSpinner.getSelectedItem().toString()) ;
//        int posicion = Integer.valueOf((String)sexSpinner.getSelectedItem()) ;
//        Log.e("Posicion:  ", String.valueOf(posicion));
//        editor.putInt(SEXO, posicion);
        editor.putString(PESO,peso.getText().toString());
        editor.putString(ALTURA,altura.getText().toString());
        editor.putString(INTOLERANCIAS,intoleranciasEdit.getText().toString());
        editor.putString(INGREDIENTES,ingredientesNoEdit.getText().toString());
        editor.apply();
    }
    //-----------------------------------------------------------------------//
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ubicacion != null) {
            savedInstanceState.putString(CLAVE_RUTA_IMAGEN, ubicacion.getPath());
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    //-----------------------------------------------------------------------//
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CLAVE_RUTA_IMAGEN)) {
            ubicacion = new File(savedInstanceState.getString(CLAVE_RUTA_IMAGEN));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
    //-----------------------------------------------------------------------//
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
    //-----------------------------------------------------------------------//
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
    //-----------------------------------------------------------------------//
    // method for bitmap to base64
    // https://stackoverflow.com/questions/18072448/how-to-save-image-in-shared-preference-in-android-shared-preference-issue-in-a
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    //-----------------------------------------------------------------------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
    //-----------------------------------------------------------------------//
    public void onTextViewClickedIntolerancias(View v){

        //Código extraido de:
        //https://android--examples.blogspot.com/2015/04/alertdialog-in-android.html
        final TextView tv = (TextView) findViewById(R.id.intoleranciasEdit);
        tv.setText(null);
        //Initialize a new AlertDialog Builder
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Seleccione las alergias e intolerancias:");

        final String[] Intolerancias = new String[]{
                "Lactosa","Histamina","Gluten","Alergia al marisco y al pescado", "Alergia a verduras y frutas","Alergia a frutos secos, legumbres y cereales"
        };

        //ArrayList to store Alert Dialog selected items index position
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        //Array to store pre checked/selected items
        final boolean[] preCheckedItems = new boolean[]{
                false,false,false,false,false,false
        };

        adb.setMultiChoiceItems(Intolerancias, preCheckedItems, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked){

                //You can update the preCheckedItems array here
                //In this tutorial i ignored this feature
                if(isChecked)
                {
                    //Add the checked item to checked items collection
                    selectedItems.add(which);
                }
                else if(selectedItems.contains(which))
                {
                    /*If the clicked checkbox item is unchecked now
                    and it already contains in the selected items collection
                    then we remove it from selected items collection*/
                    selectedItems.remove(which);
            }
            }
        });

        //Define the AlertDialog positive/ok/yes button
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //When user click the positive button from alert dialog

                //Loop/iterate through ArrayList
                for(int i=0;i<selectedItems.size();i++){
                    //selectedItems ArrayList current item's correspondent
                    int IndexOfAllergies = selectedItems.get(i);

                    //Get the selectedItems array specific index position's
                    String intoleranciaSelected = Arrays.asList(Intolerancias).get(IndexOfAllergies);

                    // Borramos el contenido ya almacenado y Display the selected colors to TextView
                    tv.setText(tv.getText() + "   "+ intoleranciaSelected + "\n");


                }
            }
        });

        //Define the Neutral/Cancel button in AlertDialog
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //When user click the neutral/cancel button from alert dialog
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();
    }

    //-----------------------------------------------------------------------//

    public void onTextViewClickedIngredientes(View v){

        //Código extraido de:
        //https://android--examples.blogspot.com/2015/04/alertdialog-in-android.html
        final TextView tv = (TextView) findViewById(R.id.ingredientesNoEdit);
        tv.setText(null);

        //Initialize a new AlertDialog Builder
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //Set a title for alert dialog
        adb.setTitle("Seleccione los ingredientes no deseados:");

        //Initialize a new String Array
        final String[] Ingredientes = new String[]{
                "Huevos","Leche","Gluten","Frutos Secos","Soja", "Marisco"
        };

        //ArrayList to store Alert Dialog selected items index position
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        //Array to store pre checked/selected items
        final boolean[] preCheckedItems = new boolean[]{
                false,false,false,false,false,false
        };

        adb.setMultiChoiceItems(Ingredientes, preCheckedItems, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked){

                //You can update the preCheckedItems array here
                //In this tutorial i ignored this feature
                if(isChecked)
                {
                    //Add the checked item to checked items collection
                    selectedItems.add(which);
                }
                else if(selectedItems.contains(which))
                {
                    /*If the clicked checkbox item is unchecked now
                     and it already contains in the selected items collection
                     then we remove it from selected items collection*/
                    selectedItems.remove(which);
                }
            }
        });

        //Define the AlertDialog positive/ok/yes button
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //When user click the positive button from alert dialog

                //Loop/iterate through ArrayList
                for(int i=0;i<selectedItems.size();i++){
                    //selectedItems ArrayList current item's correspondent
                    int IndexOfIngredients = selectedItems.get(i);

                    //Get the selectedItems array specific index position's
                    String ingredientesSelected = Arrays.asList(Ingredientes).get(IndexOfIngredients);

                    //Display the selected colors to TextView
                    tv.setText(tv.getText() + "   "+ingredientesSelected + "\n");

                }
            }
        });

        //Define the Neutral/Cancel button in AlertDialog
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //When user click the neutral/cancel button from alert dialog
            }
        });
        tv.setText(null);
        //Display the Alert Dialog on app interface
        adb.show();
    }
}
//-----------------------------------------------------------------------//
