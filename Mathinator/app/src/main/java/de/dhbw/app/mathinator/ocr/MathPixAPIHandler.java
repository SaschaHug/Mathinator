package de.dhbw.app.mathinator.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MathPixAPIHandler {
    private Response response;

    /**
     * Ruft die MathPix API über OKhttp mit dem aufgenommenen Bild auf und liefert das Ergebnis als LaTeX-String zurück
     */
    public Response processSingleImage(String url){
        try{
            OkHttpClient client = new OkHttpClient();

            String base64encodedImage = encodeImageToBase64String(url);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"url\" : \"data:image/jpeg;base64,");
            stringBuilder.append(base64encodedImage);
            stringBuilder.append("\"}");

            String text = stringBuilder.toString();
            text = text.replace("\n", "").replace("\r", "");

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, text);
            Request request = new Request.Builder()
                    .url("https://api.mathpix.com/v3/latex")
                    .addHeader("content-type", "application/json")
                    .addHeader("app_id", "lamm")
                    .addHeader("app_key", "0d5b4dddff1817d479cbbe36a6a55d8c")
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Fehler in processSingleImage()");
        }
        return response;
    }

    public String encodeImageToBase64String(String URI){
        Bitmap bm = BitmapFactory.decodeFile(URI);
        if (bm == null)
            Log.i("EROR", "BitMap is null");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return  encodedImage;
    }

}
