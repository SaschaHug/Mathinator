package de.dhbw.app.mathinator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Diese Klasse ist ein Adapter. Sie legt fest wie die HistoryActivity mit Daten versorgt wird
 * (Über einen Cursor [siehe HistoryActivity] für die Erklärung)
 */

public class HistoryCursorAdapter extends CursorAdapter {
    public HistoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_history, parent, false);
    }



    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvEquation = (TextView) view.findViewById(R.id.tvEquation);
        TextView tvResult = (TextView) view.findViewById(R.id.tvResult) ;
        // Extract properties from cursor
        //String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String equation = cursor.getString(cursor.getColumnIndexOrThrow("equation"));
        String result = cursor.getString(cursor.getColumnIndexOrThrow("result"));

        // Populate fields with extracted properties
        tvEquation.setText(equation);
        tvResult.setText(result);


    }


}
