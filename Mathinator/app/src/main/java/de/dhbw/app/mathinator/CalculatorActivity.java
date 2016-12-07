package de.dhbw.app.mathinator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;

public class CalculatorActivity extends Activity {

    public Button resultButton;
    public EditText inputField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        inputField = (EditText) findViewById(R.id.InputField);

        initOnclickListener();
    }



    public void initOnclickListener()
    {
        resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                History newManualCalcEntry = new History();
                newManualCalcEntry.equation = inputField.getText().toString();

                // TODO: Prüfen ob die ID benötigt wird
                newManualCalcEntry.id = "000";

                Log.i("CalculatorActivity", " Value of InputField: " + inputField.getText().toString() );

                // Hole Instanz des dbhelpers.
                // Kontext muss 'CalculatorActivity.this' statt 'this' sein, da wir uns im onClick Listener befinden

                MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(CalculatorActivity.this);
                databaseHelper.addEntry(newManualCalcEntry);
            }
        });
    }


}
