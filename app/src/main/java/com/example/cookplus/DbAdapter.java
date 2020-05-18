package com.example.cookplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    private static final String TAG = "APMOV: DbAdapter"; // Usado en los mensajes de Log

    //Nombre de la base de datos, tablas y versión
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_LISTA = "lista";
    private static final String DATABASE_TABLE_DESPENSA = "despensa";
    private static final int DATABASE_VERSION = 2;

    //campos de las tablas de la base de datos
    public static final String KEY_FOOD= "alimento";
    public static final String KEY_CHECKED= "checked";
    public static final String KEY_ROWID = "_id";

    //Sentencias SQL para crear las tablas de las bases de datos
    private static final String DATABASE_CREATE_LISTA = "create table " + DATABASE_TABLE_LISTA + " (" +
            KEY_ROWID +" integer primary key autoincrement, " +
            KEY_FOOD +" text not null, " +
            KEY_CHECKED + " integer);";

    private static final String DATABASE_CREATE_DESPENSA = "create table " + DATABASE_TABLE_DESPENSA + " (" +
            KEY_ROWID +" integer primary key autoincrement, " +
            KEY_FOOD +" text not null, " +
            KEY_CHECKED + " integer);";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_LISTA);
            db.execSQL(DATABASE_CREATE_DESPENSA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LISTA);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_DESPENSA);
            onCreate(db);
        }
    }

    /**
     * Constructor - toma el contexto para permitir a labase de datos
     * abrirse/crearse
     *
     * @param ctx el contexto donde trabajar
     */

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abre la base de datos. Si no pudiera abrirse, intenta crear una nueva
     * instancia de la base de datos. Si no se crea, lanza una excepcion para
     * indicar el error
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Crea un nuevo item en la lista con el nombre del alimento y el estado del checkbox.
     * Si el item se crea correctamente se devuelve el rowId para ese item, en caso contrario
     * se devulve -1 indicando el fallo.
     *
     * @param tabla tabla de la bbdd donde añadir el alimento creado
     * @param alimento nombre del alimento a añadir
     * @param checked estado del checkbox
     * @return rowId o -1 si falla
     */
    public long createItem(String tabla, String alimento, int checked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FOOD, alimento);
        initialValues.put(KEY_CHECKED, checked);

        return mDb.insert(tabla, null, initialValues);
    }

    /**
     * Borrar el item con el nombre del alimento indicado
     *
     * @param tabla tabla donde borrar el alimento
     * @param alimento nombre del alimento a borrar
     * @return true si se borro correctamente, false en otro caso
     */
    public boolean deleteItem(String tabla, String alimento) {
        return mDb.delete(tabla, KEY_FOOD + " = '" + alimento +"'" , null) > 0;
    }

    /**
     * Devuelve un Cursor sobre la tabla indicada de la bbdd
     *
     * @param tabla tabla donde buscar los alimentos
     * @return Cursor sobre todos los items
     */
    public Cursor fetchAllItems(String tabla) {

        return mDb.query(tabla, new String[] {KEY_ROWID, KEY_FOOD,
                KEY_CHECKED}, null, null, null, null, null);
    }



    /**
     * Actualiza el item con los detalles indicados. El item se especifica
     * usando el nombre del alimento, y se actualiza con el nombre del alimento
     * y estado del checkbox pasados por parametro.
     *
     * @param tabla tabla donde buscar el alimento y actualizarlo
     * @param alimento nombre del alimento a actualizar
     * @param checked estado del checkbox del alimento
     * @return true si el item se actualizo correctamente, false en otro caso
     */
    public boolean updateItem(String tabla, String alimento, int checked) {
        ContentValues args = new ContentValues();
        args.put(KEY_FOOD, alimento);
        args.put(KEY_CHECKED, checked);

        return mDb.update(tabla, args, KEY_FOOD + " = '" + alimento +"'" , null) > 0;
    }

    /**
     * Borra todos los items de la tabla de la base de datos indicada con tabla
     *
     * @param tabla tabla donde borrar los alimentos
     */
    public void clear(String tabla){
        mDb.delete(tabla, null, null);
    }

}