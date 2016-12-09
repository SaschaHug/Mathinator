package de.dhbw.app.mathinator;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Wie muss der Übergabeparameter sein?
        MathinatorDatabaseHelper.getInstance(this);
        setContentView(R.layout.activity_history);


        /**
         * Im Folgenden werden Testdaten für die SQLite DB erzeugt
         */

        /**
    History sampleEntry = new History();
    sampleEntry.id = "0";
    sampleEntry.equation = "1+2*(3/4)^5";
*/

    // Get singleton instance of database
    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);
     //   databaseHelper.deleteAllEntries();
     //   databaseHelper.deleteAllEntries();



   // Add sample post to the database
    //databaseHelper.addEntry(sampleEntry);

    // Get all posts from database
   /** List<History> entries = databaseHelper.getAllEntries();
    for (History entry : entries) {
        // do something
        System.out.println("ID: " + entry.id);
        System.out.println("EQ: " + entry.equation);
    }
    */

        // Get access to the underlying writeable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back

        Cursor historyCursor = db.rawQuery("SELECT * FROM history", null);
        Log.i("HistoryActivity", "Cursor(0)" + historyCursor.getColumnName(0));
        Log.i("HistoryActivity", "Cursor(1)" + historyCursor.getColumnName(1));
        Log.i("HistoryActivity", "Cursor(2)" + historyCursor.getColumnName(2));

        Log.i("HistoryActivity", "COLUMN COUNT. " + historyCursor.getColumnCount());





        // Find ListView to populate
       ListView lvItems = (ListView) findViewById(R.id.lvItems);
        // Setup cursor adapter using cursor from last step
        HistoryCursorAdapter historyAdapter = new HistoryCursorAdapter(this, historyCursor);
        // Attach cursor adapter to the ListView
       lvItems.setAdapter(historyAdapter);


    }

}

