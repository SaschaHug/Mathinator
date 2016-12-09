package de.dhbw.app.mathinator;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;


/**
 * Diese Activity wird von HistoryActivity aufgerufen, wenn ein Item der ListView gelickt wird
 * Dabei wird die ID des entsprechenden Datensatzes übergeben
 * Diese Klasse dient zur Detailansicht der entsprechenden Einträge
 */

public class HistoryEntryActivity extends Activity {

    public TextView inputTextField;
    public TextView resultTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        setContentView(R.layout.activity_history_entry);

        inputTextField = (TextView)findViewById(R.id.tvInput);
        resultTextField = (TextView)findViewById(R.id.tvResult);


        // Hole übergebene ID des geklickten ListView Items
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("KEY_HISTORY_ID");
            Log.i("HistoryEntryActivity - ","Übergebene ID: " + value);


            List<History> entries = databaseHelper.getAllEntries();
            String input = entries.get(value).equation;
            String result = entries.get(value).result;

            inputTextField.setText(input);
            resultTextField.setText(result);


        }



    }
}
