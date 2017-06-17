package de.dhbw.app.mathinator.ocr;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Response;

public class CallApiTask extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... urls) {
        String url = urls[0];

        Log.i("test: ", urls[0].toString());

        MathPixAPIHandler apiHandler = new MathPixAPIHandler();
        Response response = apiHandler.processSingleImage(url);

        Log.i("MathPix", "doInBackground");

        Log.i("MathPix", "isSuccessful: " + response.isSuccessful());
        Log.i("MathPix", "Response: " + response.networkResponse());
        //Log.i("MathPix", response.message().toString() + response.body().toString() + response.body().contentType().toString());

       // Log.i("MathPix", response.toString());
        try {
            Log.i("MathPix", "message content: " + response.body().string());

        } catch (IOException ioException) {
            // Do magic
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

