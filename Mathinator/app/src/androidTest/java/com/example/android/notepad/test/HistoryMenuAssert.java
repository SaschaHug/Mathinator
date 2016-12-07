package com.example.android.notepad.test;

import de.dhbw.app.mathinator.Mathinator;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class HistoryMenuAssert extends ActivityInstrumentationTestCase2<Mathinator> {
  	private Solo solo;
  	
  	public HistoryMenuAssert() {
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
        //Set default small timeout to 83045 milliseconds
		Timeout.setSmallTimeout(83045);
        //Click on History
		solo.clickOnView(solo.getView(de.dhbw.app.mathinator.R.id.history_button));
        //Wait for activity: 'de.dhbw.app.mathinator.HistoryActivity'
		assertTrue("de.dhbw.app.mathinator.HistoryActivity is not found!", solo.waitForActivity(de.dhbw.app.mathinator.HistoryActivity.class));
        //Assert that: 'Gleichung' is shown
		assertTrue("'Gleichung' is not shown!", solo.waitForView(solo.getView(de.dhbw.app.mathinator.R.id.tvEquation, 5)));
	}
}
