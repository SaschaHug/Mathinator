package de.tobiaslamm.httpsmathinator.mathinator;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class ApplicationTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ApplicationTest() {
        super(Mathinator1.class);
    }

    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        Solo.Config config = new Solo.Config();
        config.commandLogging = true;
        solo = new Solo(getInstrumentation(), config, getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testMathinator1Opened() throws Exception {
        // Check that MainActivity is opened succesfully
        solo.assertCurrentActivity("Expected Main activity", "Mathinator1");
    }

    public void testHelloWorldFound() throws Exception {
        // Check that "Hello World!" text can be found from the screen
        boolean result = solo.searchText("Hello World!");
        assertEquals("Hello World! text not found", true, result);
    }
}