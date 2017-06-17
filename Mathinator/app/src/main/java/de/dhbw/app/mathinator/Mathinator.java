package de.dhbw.app.mathinator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;
import android.graphics.Color;
import android.view.Gravity;



import java.net.URL;

import de.dhbw.app.mathinator.ocr.CallApiTask;
import de.dhbw.app.mathinator.ocr.MathPixAPIHandler;
import okhttp3.Response;

/**
 * Einstiegspunkt unserer App.
 * Es werden Buttons initialisert und die zugehörigen onClickListener gestartet
 */

public class Mathinator extends Activity {

    public Button calcButton;
    public Button historyButton;
    public Button pictureButton;

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
        setContentView(R.layout.activity_mathinator);

        showCalculator();
        showHistory();
        showPicture();

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



                Intent intent = new Intent(
                        MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intent);
                Log.i("path: ", getOriginalImagePath());


                String  url = "/storage/sdcard/Download/limit.jpg";
                new CallApiTask().execute(getOriginalImagePath());


            }
        });
    }

    public String getOriginalImagePath() {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }

    private void runOverlay_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Use the History Button to get a list of your equations which were solved in the past")
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
                .playLater(historyButton);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Click on Calculate to manually type in equations you want solve")
                        .setGravity(Gravity.BOTTOM)
                )
                .setOverlay(new Overlay()
                        .setHoleRadius(1)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setPointer(new Pointer().setColor(Color.RED))
                .playLater(calcButton);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Make a Picture of an equation you want to solve")
                        .setGravity(Gravity.TOP)
                )
                .setOverlay(new Overlay()
                        .setStyle(Overlay.Style.Rectangle)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .setPointer(new Pointer().setColor(Color.RED))
                // note that there is no Overlay here, so the default one will be used
                .playLater(pictureButton);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2, tourGuide3)
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
                        .setDescription("Use the History Button to get a list of your equations which were solved in the past")
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
                .playLater(historyButton);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Click on Calculate to manually type in equations you want to solve")
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
                .playLater(calcButton);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Make a Picture of an equation you want to solve")
                        .setGravity(Gravity.TOP)
                )
                .setOverlay(new Overlay()
                        .setStyle(Overlay.Style.Rectangle)
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .setPointer(new Pointer().setColor(Color.RED))
                .playLater(pictureButton);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2, tourGuide3)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Mathinator.this, "default Overlay clicked", Toast.LENGTH_SHORT).show();
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

