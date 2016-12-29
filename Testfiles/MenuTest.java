package com.android.mathinator.test;

import de.dhbw.app.mathinator.Mathinator;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class MenuTest extends ActivityInstrumentationTestCase2<Mathinator> {
    private Solo solo;

    public MenuTest() {
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
        //Click on History
        solo.clickOnView(solo.getView(de.dhbw.app.mathinator.R.id.history_button));
        //Press menu back key
        solo.goBack();
        //Click on History
        solo.clickOnView(solo.getView(de.dhbw.app.mathinator.R.id.history_button));
        //Press menu back key
        solo.goBack();
    }
}