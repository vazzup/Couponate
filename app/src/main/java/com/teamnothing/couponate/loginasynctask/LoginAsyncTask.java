package com.teamnothing.couponate.loginasynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vazzup on 7/10/17.
 */

public class LoginAsyncTask extends AsyncTask <String, Integer, Boolean>{

    HttpURLConnection urlConnection;

    @Override
    protected Boolean doInBackground(String... params) {
        StringBuilder result = new StringBuilder();
        boolean boolResponse = false;
        try {
            URL url = new URL("http://couponate.herokuapp.com/api/v1/users");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            String jsonString = result.toString();
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray dataResponse = jsonResponse.getJSONArray("data");
            int length = dataResponse.length();
            // Log.i("JsonNumber", "Size is: " + length);
            for(int i = 0; i < length; i++) {
                JSONObject obj = dataResponse.getJSONObject(i);
                String emailID = obj.getString("email");
                // Log.i("JsonCompare", emailID.trim() + " " + params[0].trim());
                if(emailID.trim().equalsIgnoreCase(params[0].trim())) {
                    boolResponse = true;
                }
            }
        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return boolResponse;
    }
}
