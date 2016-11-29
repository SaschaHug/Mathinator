package de.dhbw.app.mathinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Mathinator extends AppCompatActivity {

    public Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathinator1);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_takeapicture);
            }
        });

        final Button button2 = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
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

