package com.teamnothing.couponate.couponretrieveasynctask;

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
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by vazzup on 7/10/17.
 */

public class CouponRetrieveAsyncTask extends AsyncTask<String, String, ArrayList<String>> {

    HttpURLConnection urlConnection;
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList <String> vals = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("https://couponate.herokuapp.com/api/v1/links");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            String jsonString = result.toString();
            JSONObject jsonResponse = new JSONObject(jsonString);
            // Log.i("JSONString", jsonString);
            JSONArray dataResponse = jsonResponse.getJSONArray("data");
            int length = dataResponse.length();
            // Log.i("JsonNumber", "Size is: " + length);
            for(int i = 0; i < length; i++) {
                JSONObject obj = dataResponse.getJSONObject(i);
                String couponCode = obj.getString("url");
                String visible = obj.getString("visible");
                if(obj.getInt("user_id") == Integer.parseInt(params[0])) {
                    // Log.i("CouponCode", couponCode + " added");
                    vals.add(couponCode);
                    // Log.i("ValsSize", "" + vals.size());
                } else {
                    String[] visiblearr = visible.split(",");
                    for (int j = 0; j < visiblearr.length; j++) {
                        if (visiblearr[j].trim().equalsIgnoreCase(params[1])) {
                            vals.add(couponCode);
                        }
                    }
                }
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return vals;
    }
}
