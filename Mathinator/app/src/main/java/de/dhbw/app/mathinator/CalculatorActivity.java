package de.dhbw.app.mathinator;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;
import de.dhbw.app.mathinator.calculator.*;
import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

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

    /*USE CASE SHOW TOUR*/
    public ChainTourGuide mTourGuideHandler;
    private Animation mEnterAnimation, mExitAnimation;
    public static final String COLOR_DEMO = "color_demo";
    public static final int OVERLAY_METHOD = 1;
    public static final int OVERLAY_LISTENER_METHOD = 2;
    public static final String CONTINUE_METHOD = "continue_method";
    private int mChosenContinueMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        inputField = (EditText) findViewById(R.id.InputField);
        resultTextField = (TextView)findViewById(R.id.resultTextField);

        initOnclickListener();

        Intent intent = getIntent();
        mChosenContinueMethod = intent.getIntExtra(CONTINUE_METHOD, OVERLAY_METHOD);
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);
        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);
        if (mChosenContinueMethod == OVERLAY_METHOD) {
            runOverlay_ContinueMethod();
        } else if (mChosenContinueMethod == OVERLAY_LISTENER_METHOD){
            runOverlayListener_ContinueMethod();
        }
    }



    public void initOnclickListener()
    {
        resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    ANTLRInputStream input = null;
                    try {
                        input = new ANTLRInputStream((inputField.getText().toString()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CalculatorLexer lexer = new CalculatorLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    CalculatorParser parser = new CalculatorParser(tokens);
                    ParseTree tree = parser.input();
                    CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
                    Double result = calcVisitor.visit(tree);
                    resultTextField.setText(result.toString());

                    Log.i("CalculatorActivity ", "Result: " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Dattensätze in die DB schreiben
                History newManualCalcEntry = new History();
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
    private void runOverlay_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Click on the Input-Field to enter your equation")
                        .setGravity(Gravity.BOTTOM)
                )
                .setOverlay(new Overlay()
                        .setHoleRadius(1)
                        //.setStyle(Overlay.Style.Rectangle)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setPointer(new Pointer().setColor(Color.RED))
                // note that there is no Overlay here, so the default one will be used
                .playLater(inputField);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Solve the equation you entered")
                        .setGravity(Gravity.BOTTOM)
                )
                .setOverlay(new Overlay()
                        .setHoleRadius(1)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setPointer(new Pointer().setColor(Color.RED))
                .playLater(resultButton);


        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();


        ChainTourGuide.init(this).playInSequence(sequence);
    }

    private void runOverlayListener_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Click on the Input-Field to enter your equation")
                        .setGravity(Gravity.BOTTOM)
                )
                .setOverlay(new Overlay()
                        .setHoleRadius(1)
                        //.setStyle(Overlay.Style.Rectangle)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setPointer(new Pointer().setColor(Color.RED))
                // note that there is no Overlay here, so the default one will be used
                .playLater(inputField);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Solve the equation you entered")
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        .setGravity(Gravity.BOTTOM)
                )
                .setOverlay(new Overlay()
                        .setHoleRadius(1)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTourGuideHandler.next();
                            }
                        })
                )
                .setPointer(new Pointer().setColor(Color.RED))
                .playLater(resultButton);


        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(CalculatorActivity.this, "default Overlay clicked", Toast.LENGTH_SHORT).show();
                                mTourGuideHandler.next();
                            }
                        })
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.OverlayListener)
                .build();

        mTourGuideHandler = ChainTourGuide.init(this).playInSequence(sequence);
    }
}