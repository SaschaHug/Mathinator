package de.dhbw.app.mathinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class Mathinator extends Activity {

    public Button calcButton;
    public Button historyButton;
    public Button pictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathinator);


        // TODO: Methoden Umbennen?
        // Die Menüs werden ja nicht direkt aufgerufen sonder es wird nur ein OnClickListener erzeugt...
        showCalculator();
        showHistory();
        showPicture();
    }


    /**
     * Initialisiere onClickListener für Buttons
     */

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

    public void showHistory()
    {
        historyButton = (Button) findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent showHistory = new Intent(Mathinator.this, HistoryActivity.class);
                startActivity(showHistory);
            }
        });
    }
    public void showPicture()
    {
        pictureButton = (Button) findViewById(R.id.camera_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent instantilieren
                Intent intent = new Intent(
                        MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                // Activity starten
                startActivity(intent);
            }
        });
    }
}

