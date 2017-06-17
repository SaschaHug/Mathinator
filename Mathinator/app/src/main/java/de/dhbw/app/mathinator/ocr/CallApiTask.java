package de.dhbw.app.mathinator.ocr;


import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import de.dhbw.app.mathinator.calculator.CalculatorBaseVisitorImpl;
import de.dhbw.app.mathinator.calculator.CalculatorLexer;
import de.dhbw.app.mathinator.calculator.CalculatorParser;
import de.dhbw.app.mathinator.database.History;
import de.dhbw.app.mathinator.database.MathinatorDatabaseHelper;
import okhttp3.Response;

public class CallApiTask extends AsyncTask<String, Integer, Long> {
    protected Context context;

    public CallApiTask(Context context){
        this.context = context.getApplicationContext();
    }


    protected Long doInBackground(String... urls) {
        String url = urls[0];

        try {
        MathPixAPIHandler apiHandler = new MathPixAPIHandler();
        Response response = apiHandler.processSingleImage(url);

            Log.i("MathPix", "Response: " + response.networkResponse());
            String responseString = response.body().string();
            Log.i("MathPix", "message content: " + responseString);

            DetectionResult detectionResult = new Gson().fromJson(responseString, DetectionResult.class);

                if (detectionResult != null && detectionResult.latex != null){
                    String equation = detectionResult.latex;

                    // Calculate Result
                    ANTLRInputStream input = null;
                    try {
                        input = new ANTLRInputStream(equation);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CalculatorLexer lexer = new CalculatorLexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    CalculatorParser parser = new CalculatorParser(tokens);
                    ParseTree tree = parser.input();
                    CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
                    Double result = calcVisitor.visit(tree);


                    // Datensätze in die DB schreiben
                    History newEntry = new History();
                    newEntry.equation = equation;
                    newEntry.result = result.toString();

                    // Hole Instanz des dbhelpers.
                    MathinatorDatabaseHelper databaseHelper = MathinatorDatabaseHelper.getInstance(context);
                    databaseHelper.addEntry(newEntry);
                }

            } catch (Exception e){
                System.err.println("Something went wrong..");
                e.printStackTrace();

            }



        long totalSize = 0;
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
        //Log.i("MathPix", "onProgressUpdate");
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
        //Log.i("MathPix", "onPostExecute");
    }
}

