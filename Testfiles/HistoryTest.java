package com.android.mathinator.test;

import de.dhbw.app.mathinator.Mathinator;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class HistoryTest extends ActivityInstrumentationTestCase2<Mathinator> {
    private Solo solo;

    public HistoryTest() {
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
        //Wait for activity: 'de.dhbw.app.mathinator.HistoryActivity'
        assertTrue("de.dhbw.app.mathinator.HistoryActivity is not found!", solo.waitForActivity(de.dhbw.app.mathinator.HistoryActivity.class));
        //Click on ListView 1+2*3 7.0
        solo.clickInList(1, 0);
        //Wait for activity: 'de.dhbw.app.mathinator.HistoryEntryActivity'
        assertTrue("de.dhbw.app.mathinator.HistoryEntryActivity is not found!", solo.waitForActivity(de.dhbw.app.mathinator.HistoryEntryActivity.class));
        //Press menu back key
        solo.goBack();
    }
}