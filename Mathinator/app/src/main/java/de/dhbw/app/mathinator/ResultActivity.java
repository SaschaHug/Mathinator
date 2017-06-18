package de.dhbw.app.mathinator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;


public class ResultActivity extends Activity {

    public TextView inputTextField;
    public TextView resultTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);

        setContentView(R.layout.activity_history_entry);

        inputTextField = (TextView)findViewById(R.id.tvInput);
        resultTextField = (TextView)findViewById(R.id.tvResult);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String equation = extras.getString("EQUATION");
            String result = extras.getString("RESULT");


            Log.i("ResultActivity", equation + result);
            //int value = extras.getInt("KEY_HISTORY_ID");
            //Log.i("HistoryEntryActivity - ","Übergebene ID: " + value);


            //List<History> entries = databaseHelper.getAllEntries();
            //String input = entries.get(value).equation;
            //String result = entries.get(value).result;

            inputTextField.setText(equation);
            resultTextField.setText(result);
        } else
            Log.i("ResultActivity", "Error, extras is Null");
    }
}