package com.mgl.qgui.req;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mgl.qgui.MainActivity;
import com.mgl.qgui.R;
import com.mgl.qgui.managers.TestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendToCheckTask extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;

    public SendToCheckTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private static final String TAG = "SendToCheckTask";

    @Override
    protected String doInBackground(String... params) {
        String id = params[0];
        String testId = params[1];
        String answer = params[2];
        String training = params[3];
        String cookie = params[4];
        JSONObject jsonResponse = null;

        String requestUrl = "http://10.10.9.100:8080/sendAnswer";
        String requestBody = createRequestBody(Integer.valueOf(id), Integer.valueOf(testId), answer,training);

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

                JSONObject finalJsonResponse = jsonResponse;
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if(finalJsonResponse.get("response").equals("Good")) {
                                ((TextView)mainActivity.findViewById(R.id.question_text)).setText("Souhlasím!");
                                ((TextView)mainActivity.findViewById(R.id.question_text)).setTextColor(Color.GREEN);
                            } else {
                                ((TextView)mainActivity.findViewById(R.id.question_text)).setText("Nesouhlasím!");
                                ((TextView)mainActivity.findViewById(R.id.question_text)).setTextColor(Color.RED);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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
    public String createRequestBody(int id,int testId,String answer,String training){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("questionsID", id);
            jsonObject.put("testID", testId);
            jsonObject.put("answer", answer);
            jsonObject.put("training", training);
            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e(TAG, "Chyba při vytváření JSON těla požadavku: " + e.getMessage());
            return null;
        }
    }
}