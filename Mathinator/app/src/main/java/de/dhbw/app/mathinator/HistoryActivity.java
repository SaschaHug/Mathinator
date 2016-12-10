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

public class HistoryActivity extends Activity {
    // Tracks current contextual action mode
    private ActionMode currentActionMode;

    // Tracks current menu item
    public int currentListItemIndex;

    // Define the callback when ActionMode is activated
    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_textview, menu);
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            //    case R.id.menu_edit:
            //        Toast.makeText(MainActivity.this, "Editing!", Toast.LENGTH_SHORT).show();
            //        mode.finish(); // Action picked, so close the contextual menu
            //        return true;
                case R.id.menu_delete:
                    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(HistoryActivity.this);
                    // TODO: Trigger the deletion here
                    Toast.makeText(HistoryActivity.this, "Deleting!", Toast.LENGTH_SHORT).show();
                    databaseHelper.deleteEntry(currentListItemIndex);
                    //databaseHelper.deleteAllEntries();
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


                    // Startet die Activity einfach neu, damit sich der ListView aktualisiert....
                    // finish();
                    // startActivity(getIntent());
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
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

        //Im Folgenden werden Testdaten für die SQLite DB erzeugt
    /**
    History sampleEntry = new History();
    sampleEntry.id = "0";
    sampleEntry.equation = "1+2*(3/4)^5";
    */

    // Get singleton instance of database
    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);

    // Add sample post to the database
    // databaseHelper.addEntry(sampleEntry);
    // Get all posts from database
    List<History> entries = databaseHelper.getAllEntries();
    for (History entry : entries) {
        System.out.println("ID: " + entry.id);
        System.out.println("EQ: " + entry.equation);
        System.out.println("Re: " + entry.result);
    }

        // Get access to the underlying writeable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back
        final Cursor historyCursor = db.rawQuery("SELECT * FROM history", null);

      /**
        Log.i("HistoryActivity", "Cursor(0)" + historyCursor.getColumnName(0));
        Log.i("HistoryActivity", "Cursor(1)" + historyCursor.getColumnName(1));
        Log.i("HistoryActivity", "Cursor(2)" + historyCursor.getColumnName(2));
        Log.i("HistoryActivity", "COLUMN COUNT. " + historyCursor.getColumnCount());
*/

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
                //Log.i("TEST", "Position: " + position + " CursorAdapter Pos.: " + historyCursor.getPosition());
                //Übergebe ID des geklickten ListView Items
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

