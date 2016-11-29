package de.dhbw.app.mathinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Mathinator extends Activity {


    public Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathinator);

        final Button camera_button = (Button) findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_takeapicture);
            }
        });

        final Button history_button = (Button) findViewById(R.id.history_button);
        history_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_showhistory);
            }
        });


        showCalculator();
    }


    public void showCalculator()
    {
        calcButton = (Button) findViewById(R.id.calculator_button);
        calcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent showCalc = new Intent(Mathinator.this, CalculatorActivity.class);
                startActivity(showCalc);
            }
        });

    }
}

