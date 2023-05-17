package com.mgl.qgui.req;

import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import com.mgl.qgui.MainActivity;
import com.mgl.qgui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginRequestTask extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;

    public LoginRequestTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private static final String TAG = "LoginRequestTask";

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String password = params[1];

        String requestUrl = "http://10.10.9.100:8080/login";
        String requestBody = createLoginRequestBody(username, password);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(500);
            connection.setRequestMethod("POST");
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

                String cookies = connection.getHeaderField("Set-Cookie");

                return cookies;
            } else {
                Log.e(TAG, "Neúspěšný požadavek, chybový kód: " + responseCode);
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mainActivity.getApplicationContext(),"Neúspěšný požadavek, heslo nebo uži. jméno bylo zadano špatně!",Toast.LENGTH_LONG).show();

                    }
                });
            }
        } catch (IOException e) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(),"Chyba při provádění požadavku: " + e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
            Log.e(TAG, "Chyba při provádění požadavku: " + e.getMessage());
        }

        return null;
    }
    @Override
    protected void onPostExecute(String cookies) {
        if (cookies != null) {
            Log.d(TAG, "Cookies: " + cookies);
            mainActivity.setCookies(cookies);
            mainActivity.setContentView(R.layout.activity_menu);

        } else {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(),"Chyba při získávání auth klíče",Toast.LENGTH_LONG).show();

                }
            });
            Log.e(TAG, "Chyba při získávání cookies");
        }
    }
    private String createLoginRequestBody(String username, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", username);
            jsonObject.put("pass", password);
            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e(TAG, "Chyba při vytváření JSON těla požadavku: " + e.getMessage());
            return null;
        }
    }
}
