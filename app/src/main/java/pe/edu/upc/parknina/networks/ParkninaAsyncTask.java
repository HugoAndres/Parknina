package pe.edu.upc.parknina.networks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParkninaAsyncTask {

    public static HttpURLConnection connectionConfigure(String resource, String action) {
        try {
            URL url = new URL(ParkninaApi.getApi_url() + resource);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(action);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            return urlConnection;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void sendHeadersAndMethod(HttpURLConnection urlConnection, String jsonRequest) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonRequest);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String result;
        while((result = bufferedReader.readLine()) != null)
            stringBuilder.append(result).append("\n");

        if (stringBuilder.length() == 0) {
            return null;
        }

        try {
            bufferedReader.close();
        } catch (final IOException e) {
            Log.e("TAG", "Error closing stream", e);
        }
        inputStream.close();

        return stringBuilder.toString();
    }
}
