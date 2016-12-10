package de.dhbw.app.mathinator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;
import de.dhbw.app.mathinator.calculator.*;

/**
 * Diese Klasse ist für die Berechnung der Eingabe (sowohl für manuell eingegebene Gleichungen,
 * als auch für die später automatisch erkannten zuständig.
 * Aktuell ist hier natürlich nur der manuelle "Taschenrechner" implementiert
 *
 * Realisiert wird dieser über ANTLR ( = Another Tool for Language Recognition)
 * ANTLR ist ein Parsergenerator, der das Generieren von Lexer und Parser übernimmt.
 * Erzeugt werden diese aus einem Grammer File (Calculator.g4 im Package calculator)
 * Dort können in EBNF-ähnlicher Form Kontextfreie (und ich glaube auch reguläre Grammatiken definert werden)
 * Es existiert ein IntelliJ-Plugin womit man Lexer u Parser generieren kann,
 * Android Studio bietet euch an dieses zu installieren sobald ihr die Datei öffnet
 *
 * Nach dem generieren hat der Parser allerding noch keine von uns definerte Funktion (er ist also nur Akzeptor / )
 * Er kann aber Parsebäume erzeugen und beliebig erweitert werden. Dies geschieht in der Klasse
 * CalculatorBaseVisitorImplementation. Die Klasse geht die Knoten des Parsebaumes durch und erledigt die Berechnung für uns
 *
 * Vorrangregeln lassen sich aus der Grammatik ableiten (muss beim Erweitern der Grammatik natürlich beachtet werden)
 */
public class CalculatorActivity extends Activity {

    public Button resultButton;
    public EditText inputField;
    public TextView resultTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        inputField = (EditText) findViewById(R.id.InputField);
        resultTextField = (TextView)findViewById(R.id.resultTextField);

        initOnclickListener();
    }



    public void initOnclickListener()
    {
        resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                // ANTLR Parser
                try {
                    // Um unsere Zeichenkette verarbeiten zu können muss diese eingelesen werden.
                    ANTLRInputStream input = null;
                    try {
                        input = new ANTLRInputStream((inputField.getText().toString()));

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    // Lexer erstellen und mit input versorgen
                    CalculatorLexer lexer = new CalculatorLexer(input);

                    // Der Lexer generiert unsere Tokens aus dem Input (z.B. sind einzelne Zahlen oder Operatoren Tokens
                    CommonTokenStream tokens = new CommonTokenStream(lexer);

                    // Parser erstellen und Tokens übergeben
                    CalculatorParser parser = new CalculatorParser(tokens);

                    // ParseTree erstellen und Startregel angeben
                    ParseTree tree = parser.input();

                    // Die Klasse CalculatorBaseVisitorImpl geht die einzelnen Nodes des ParseTrees durch und wertet sie aus
                    CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
                    Double result = calcVisitor.visit(tree);

                    //resultTextField.setText(inputField.toString() + " = " + result);
                    resultTextField.setText(result.toString());

                    Log.i("CalculatorActivity ", "Result: " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Dattensätze in die DB schreiben
                History newManualCalcEntry = new History();
                // TODO: Prüfen ob die ID benötigt wird
                //newManualCalcEntry.id = "000";
                newManualCalcEntry.equation = inputField.getText().toString();
                newManualCalcEntry.result = resultTextField.getText().toString();

                Log.i("CalculatorActivity", " Value of InputField: " + inputField.getText().toString() );

                // Hole Instanz des dbhelpers.
                // Kontext muss 'CalculatorActivity.this' statt 'this' sein, da wir uns im onClick Listener befinden
                MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(CalculatorActivity.this);
                databaseHelper.addEntry(newManualCalcEntry);

            }
        });
    }


}
