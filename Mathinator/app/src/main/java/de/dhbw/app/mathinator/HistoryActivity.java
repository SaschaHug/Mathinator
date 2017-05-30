package de.dhbw.app.mathinator;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;


/**
 * Diese Klasse implementiert das History-Menü.
 * Es wird eine ListView erzeugt, welche mit Daten aus der DB befüllt wird.
 * Dazu holen wir uns die Singleton-Instanz unseres DB-Helpers
 * (Singleton: dient dem Zweck, dass es über die gesamte App nur ein einziges Objekt der Datenbank gibt.
 * Gäbe es mehrere, wäre das anfällig für Konflikte beim Schreiben / Lesen)
 *
 * Einträge sind klickbar und starten ein neues Fenster welches die Daten des ausgewählten Eintrages im Detail darstellen.
 * Dies wird über einen onClickListener realisiert.
 * Der übergibt auch den jeweiligen Primary Key des Datensatzes an HistoryEntryActivity.
 *
 * Zusätzlich gibt es einen ActionMode, der es dem User erlaubt bestimmte Aktionen innerhalb der Activity auszuführen.
 * In unserem Fall soll es möglich sein Einträge zu löschen.
 * Ist dies geschehen wird der Cursor neu erzeugt.
 * Der Cursor wird dazu verwendet über eine Liste aller Datensätze zu iterieren, bzw in diesem Fall um sie neu auszulesen.
 *
 */

public class HistoryActivity extends Activity {
    private ActionMode currentActionMode;
    public int currentListItemIndex;

    // Define the callback when ActionMode is activated
    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_textview, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(HistoryActivity.this);
                    Toast.makeText(HistoryActivity.this, "Deleting!", Toast.LENGTH_SHORT).show();
                    databaseHelper.deleteEntry(currentListItemIndex);
                    mode.finish(); // Action picked, so close the contextual menu

                    /**
                     * Damit die ListView sich automatisch aktualisiert sobald ein Eintrag
                     * gelöscht wird, erzeugen wir momentan einfach einen neuen Cursor.
                     * TODO: Wird der alte Cursor geschlossen? Performance? Fehleranfällig?
                     * Sauber gestalten!!!
                     */
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    final Cursor historyCursor = db.rawQuery("SELECT * FROM history", null);
                    ListView lvItems = (ListView) findViewById(R.id.lvItems);
                    final HistoryCursorAdapter historyAdapter = new HistoryCursorAdapter(HistoryActivity.this, historyCursor);
                    lvItems.setAdapter(historyAdapter);

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null; // Clear current action mode
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Wie muss der Übergabeparameter sein?
        MathinatorDatabaseHelper.getInstance(this);
        setContentView(R.layout.activity_history);

    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);

    List<History> entries = databaseHelper.getAllEntries();
    for (History entry : entries) {
        System.out.println("ID: " + entry.id);
        System.out.println("EQ: " + entry.equation);
        System.out.println("Re: " + entry.result);
    }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back
        final Cursor historyCursor = db.rawQuery("SELECT * FROM history", null);


        // Find ListView to populate
       ListView lvItems = (ListView) findViewById(R.id.lvItems);
        // Setup cursor adapter using cursor from last step
        final HistoryCursorAdapter historyAdapter = new HistoryCursorAdapter(this, historyCursor);
        // Attach cursor adapter to the ListView
       lvItems.setAdapter(historyAdapter);



        // Einträge sollen anklickbar sein
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), HistoryEntryActivity.class);
                intent.putExtra("KEY_HISTORY_ID", position);
                startActivity(intent);


                // TODO Notwendig?
                //historyCursor.close();
            }
        });


        // Einträge sollen löschbar sein (langer Klick)
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO: Warum wird der Cursor hier beendet?
                historyCursor.close();
                if (currentActionMode != null) { return false; }
                // TODO: Hier wird ein long auf int gecastet. Fehleranfällig?
                currentListItemIndex = (int) id;//historyCursor.getPosition();//position;
                currentActionMode = startActionMode(modeCallBack);


                view.setSelected(true);
                return true;
            }
        });

    }

}