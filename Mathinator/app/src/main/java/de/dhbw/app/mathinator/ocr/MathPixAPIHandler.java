package de.dhbw.app.mathinator.ocr;

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
    public Response processSingleImage(){
        try{
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{ \"url\" : \"data:image/jpeg;base64,{BASE64-STRING}\" }");
            Request request = new Request.Builder()
                    .url("https://api.mathpix.com/v3/latex")
                    .addHeader("content-type", "application/json")
                    .addHeader("app_id", "mathpix")
                    .addHeader("app_key", "34f1a4cea0eaca8540c95908b4dc84ab")
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Fehler in processSingleImage()");
        }
        return response;
    }

}
