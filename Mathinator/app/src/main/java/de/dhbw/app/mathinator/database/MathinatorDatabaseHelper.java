package de.dhbw.app.mathinator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Die Klasse verwaltet DB-Operationen wie das Erstellen, Aktualisieren, Lesen oder Schreiben
 * Um mit der Datenbank zu interagieren muss an entsprechenden Stellen im Code eine Instanz des DBHelpers erzeugt werden:
 * getInstance(Context context)
 */

// TODO: Die Methoden getReadalbeDatabase() / getWritableDatabase() in eigene Threads auslagern

/**
 * Important Note: The SQLite database is lazily initialized.
 * This means that it isn't actually created until it's first accessed through a call to getReadableDatabase() or getWriteableDatabase().
 * This also means that any methods that call getReadableDatabase() or getWriteableDatabase() should be done on a background thread
 * as there is a possibility that they might be kicking off the initial creation of the database.
 */

public class MathinatorDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     * Ist beim Aufruf von getInstance noch keine DB vorhanden, so wird sie erzeugt.
     * Existiert sie bereits, wird diese einfach zurückgegeben
     */
    private MathinatorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static MathinatorDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "mathinatorDatabase";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_HISTORY = "history";

    // Post Table Columns
    private static final String KEY_HISTORY_ID = "_id"; // muss _id heißen, da die Klasse 'Cursor' dieses benötigt um über Einträge zu iterieren

    private static final String KEY_HISTORY_EQUATION = "equation";



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
                KEY_HISTORY_ID + " _id INTEGER PRIMARY KEY," + // Define a primary key
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
     * Die folgenden Methoden werden benutzt um CRUD-Operationen durchzuführen
     * CRUD = Create, Read, Update, Delete
     */

    // Insert a Entry into the database
    public void addEntry(History history) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO Anpassen. Einträge können eig nicht schon vorhanden sein, jeder Eintrag soll ja eine eindeutige ID bekommen
            // long entryId = addOrUpdateEntry(history);
            // long entryId = addOrUpdateEntry(history.id);

            ContentValues values = new ContentValues();
            //values.put(KEY_POST_USER_ID_FK, userId);
            //values.put(KEY_HISTORY_ID, entryId);
            //values.put(KEY_HISTORY_ID, history.id);
            values.put(KEY_HISTORY_EQUATION, history.equation);
            //values.put(KEY_POST_TEXT, post.text);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_HISTORY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }


    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    // TODO: Notwendig?
    public long addOrUpdateEntry(History history) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            //values.put(KEY_HISTORY_ID, history.id);
            values.put(KEY_HISTORY_EQUATION, history.equation);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_HISTORY, values, KEY_HISTORY_ID + "= ?", new String[]{history.id});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_HISTORY_ID, TABLE_HISTORY, KEY_HISTORY_EQUATION);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(history.equation)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_HISTORY, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }



    // Kann z.B. benutzt werden um über eine Liste aller Datensätze zu iterieren
    public List<History> getAllEntries() {
        List<History> entries = new ArrayList<>();

        String HISTORY_SELECT_QUERY =
                  String.format("SELECT * FROM %s",
                          TABLE_HISTORY);
        // Weitere Felder und Fremdschlüssel aktuell nicht vorhanden
        /*        String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_POSTS,
                        TABLE_USERS,
                        TABLE_POSTS, KEY_POST_USER_ID_FK,
                        TABLE_USERS, KEY_USER_ID);
        */


        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(HISTORY_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    History newEntry = new History();
                    newEntry.id = cursor.getString(cursor.getColumnIndex(KEY_HISTORY_ID));
                    newEntry.equation = cursor.getString(cursor.getColumnIndex(KEY_HISTORY_EQUATION));

                    entries.add(newEntry);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return entries;
    }


    // Update the entry
    // TODO Wird eigentlich nicht benötigt.
    public int updateEntry(History history) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY_EQUATION, history.equation);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_HISTORY, values, KEY_HISTORY_ID + " = ?",
                new String[] { String.valueOf(history.id) });
    }




    public void deleteAllEntries() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_HISTORY, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}
