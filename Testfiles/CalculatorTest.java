package com.android.mathinator.test;

import de.dhbw.app.mathinator.Mathinator;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class CalculatorTest extends ActivityInstrumentationTestCase2<Mathinator> {
    private Solo solo;

    public CalculatorTest() {
        super(Mathinator.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        //Wait for activity: 'de.dhbw.app.mathinator.Mathinator'
        solo.waitForActivity(de.dhbw.app.mathinator.Mathinator.class, 2000);
        //Set default small timeout to 12149 milliseconds
        Timeout.setSmallTimeout(12149);
        //Click on Calculator
        solo.clickOnView(solo.getView(de.dhbw.app.mathinator.R.id.calculator_button));
        //Wait for activity: 'de.dhbw.app.mathinator.CalculatorActivity'
        assertTrue("de.dhbw.app.mathinator.CalculatorActivity is not found!", solo.waitForActivity(de.dhbw.app.mathinator.CalculatorActivity.class));
        //Set default small timeout to 25760 milliseconds
        Timeout.setSmallTimeout(25760);
        //Enter the text: '1+2*3'
        solo.clearEditText((android.widget.EditText) solo.getView(de.dhbw.app.mathinator.R.id.InputField));
        solo.enterText((android.widget.EditText) solo.getView(de.dhbw.app.mathinator.R.id.InputField), "1+2*3");
        //Click on com.android.mathinator.R.id.resultButton
        solo.clickOnView(solo.getView(de.dhbw.app.mathinator.R.id.resultButton));
        //Press menu back key
        solo.goBack();
    }
}