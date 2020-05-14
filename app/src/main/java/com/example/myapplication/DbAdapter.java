package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    private static final String TAG = "APMOV: DbAdapter"; // Usado en los mensajes de Log

    //Nombre de la base de datos, tablas (en este caso una) y versión
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_LISTA = "lista";
    private static final String DATABASE_TABLE_DESPENSA = "despensa";
    private static final int DATABASE_VERSION = 2;

    //campos de las tablas de la base de datos
    public static final String KEY_FOOD= "alimento";
    public static final String KEY_CHECKED= "checked";
    public static final String KEY_ROWID = "_id";

    // Sentencia SQL para crear las tablas de las bases de datos
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
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
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
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param tabla table where we insert alimentos
     * @param alimento the title of the note
     * @param checked the body of the note
     * @return rowId or -1 if failed
     */
    public long createItem(String tabla, String alimento, int checked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FOOD, alimento);
        initialValues.put(KEY_CHECKED, checked);

        return mDb.insert(tabla, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param tabla table where we delete alimentos
     * @param alimento name of food to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteItem(String tabla, String alimento) {
        return mDb.delete(tabla, KEY_FOOD + " = '" + alimento +"'" , null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @param tabla table where we search alimentos
     * @return Cursor over all notes
     */
    public Cursor fetchAllItems(String tabla) {

        return mDb.query(tabla, new String[] {KEY_ROWID, KEY_FOOD,
                KEY_CHECKED}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given name
     *
     * @param tabla table where we search alimentos
     * @param name id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchItem(String tabla, String name) throws SQLException {

        Cursor mCursor =
                mDb.query(true, tabla, new String[] {KEY_ROWID,
                                KEY_FOOD, KEY_CHECKED}, KEY_FOOD + "= '" + name + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param tabla table where we search alimentos
     * @param alimento value to set note title to
     * @param checked value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateItem(String tabla, String alimento, int checked) {
        ContentValues args = new ContentValues();
        args.put(KEY_FOOD, alimento);
        args.put(KEY_CHECKED, checked);

        return mDb.update(tabla, args, KEY_FOOD + " = '" + alimento +"'" , null) > 0;
    }
    /**
     * Clear all the items at the specified table using tabla
     *
     * @param tabla table where we search alimentos
     */
    public void clear(String tabla){
        mDb.delete(tabla, null, null);
    }

}