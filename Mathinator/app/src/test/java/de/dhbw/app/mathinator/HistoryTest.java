package de.dhbw.app.mathinator;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;

import static org.junit.Assert.assertTrue;

public class HistoryTest {
    private MathinatorDatabaseHelper db;
    Context context;

    @Before
    public void setUp() throws Exception{
        //context = new HistoryTest().context;
        context = new HistoryActivity().getApplicationContext();
        context = new HistoryTest().context;
        db = MathinatorDatabaseHelper.getInstance(context);
    }

/*
    @Test
    public void item_isDeleted() throws Exception{
        assertTrue(db.deleteEntry(1));
    }

    @Test
    public void item_isCreated() throws Exception{
        //assertEquals(4, 2 + 2);
        History h = new History();
        h.equation = "1+1";
        h.result = "2";

        assertTrue(db.addEntry(h));
    }
    */
}
