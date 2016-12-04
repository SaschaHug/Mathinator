package de.dhbw.app.mathinator;

import android.app.Activity;
import android.os.Bundle;

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

    History sampleEntry = new History();
    sampleEntry.id = "0";
    sampleEntry.equation = "1+1";


    // Get singleton instance of database
    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);

    // Add sample post to the database
    databaseHelper.addEntry(sampleEntry);

    // Get all posts from database
    List<History> entries = databaseHelper.getAllEntries();
    for (History entry : entries) {
        // do something
        System.out.println("ID: " + entry.id);
        System.out.println("ID: " + entry.equation);
    }
}

}

