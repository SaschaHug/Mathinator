package de.dhbw.app.mathinator;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;

public class CalculatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // MathinatorDatabaseHelper.getInstance(this);
        setContentView(R.layout.activity_calculator);

        // Get Singleton Instance
        MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(this);

        // Get Read-Write Access to database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        History newManualCalcEntry = new History();
        newManualCalcEntry.equation = findViewById((R.id.InputField)).toString();

    }


}
