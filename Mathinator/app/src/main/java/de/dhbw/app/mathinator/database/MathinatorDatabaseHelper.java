package de.dhbw.app.mathinator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.dhbw.app.mathinator.Mathinator;

/**
 * Created by tobi on 11/30/16.
 *
 * Die Klasse verwaltet DB-Operationen wie das Erstellen, Aktualisieren, Lesen oder Schreiben
 * Die Operationen werden mit Hilfe des SQLiteOpenHelper definiert
 * 
 */


/**
 * Important Note: The SQLite database is lazily initialized.
 * This means that it isn't actually created until it's first accessed through a call to getReadableDatabase() or getWriteableDatabase().
 * This also means that any methods that call getReadableDatabase() or getWriteableDatabase() should be done on a background thread
 * as there is a possibility that they might be kicking off the initial creation of the database.
 */

public class MathinatorDatabaseHelper extends SQLiteOpenHelper {
    private static MathinatorDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "mathinatorDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_HISTORY = "history";

    // Post Table Columns
    private static final String KEY_HISTORY_ID = "id";
    private static final String KEY_HISTORY_EQUATION = "equation";


//    public MathinatorDatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
       // db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUERIES_TABLE = "CREATE TABLE " + TABLE_HISTORY +
                "(" +
                KEY_HISTORY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_HISTORY_EQUATION + " TEXT" +
                ")";

        db.execSQL(CREATE_QUERIES_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            onCreate(db);
        }
    }

    /**
     * Hier wird das Singleton Entwurfsmuster erzeugt
     * Das bedeutet, dass es nur EIN Objekt der Klasse innerhalb der Applikation geben kann / soll
     *
     */

    public static synchronized MathinatorDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MathinatorDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     * Ist beim Aufruf von getInstance noch keine DB vorhanden, so wird sie erzeugt.
     * Existiert sie bereits, wird sie einfach zurückgegeben
     */
    private MathinatorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
