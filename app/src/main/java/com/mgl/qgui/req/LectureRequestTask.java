package com.mgl.qgui.req;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mgl.qgui.MainActivity;
import com.mgl.qgui.managers.TestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LectureRequestTask extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    public LectureRequestTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private static final String TAG = "LectureRequestTask";

    @Override
    protected String doInBackground(String... strings) {
        String level = strings[0];
        String cookie = strings[1];

        JSONObject jsonResponse = null;

        String requestUrl = "http://10.10.9.100:8080/genForLevel";
        String requestBody = null;
        try {
            requestBody = new JSONObject().put("level",level).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                jsonResponse = new JSONObject(response.toString());
                mainActivity.setCurrentTest(jsonResponse);
                System.out.println(jsonResponse);
                JSONObject finalJsonResponse = jsonResponse;
                mainActivity.runOnUiThread(() -> {
                    try {
                        TestManager testManager = new TestManager(mainActivity, finalJsonResponse, true);
                        mainActivity.setTestManager(testManager);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });

            } else {
                Log.e(TAG, "Neúspěšný požadavek, chybový kód: " + responseCode);
                mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity.getApplicationContext(),"Neúspěšný požadavek, chybový kód: " + responseCode,Toast.LENGTH_LONG).show());
            }
        } catch (IOException | JSONException e) {
            mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity.getApplicationContext(),"Chyba při provádění požadavku: " + e.getMessage(),Toast.LENGTH_LONG).show());
            Log.e(TAG, "Chyba při provádění požadavku: " + e.getMessage());
        }

        return null;
    }
}
