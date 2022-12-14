package com.wddonline.minigolf.myminigolfscorecard;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mobile World on 6/22/2018.
 */
public class ParseURL extends AsyncTask<String, Void, String> {

    FindLocationActivity context;
    ParseURL(FindLocationActivity context ){
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result_str) {
        super.onPostExecute(result_str);
        Log.e("TAG",result_str);
        if(result_str.equals("")){
            context.onFailedLoadingData();
            return;
        }

        try {
            JSONArray resultArray = new JSONArray(result_str);
            context.onLoadedData(resultArray);

//            JSONObject resultObject = new JSONObject(result_str);
//            String state = resultObject.getString("state");
//            if( !state.equals("100")) {
//                Log.e("TAG","Failed!");
////                showAlert();
//                return;
//            }
//            JSONArray listObject = resultObject.getJSONObject("retData").getJSONArray("listData");
//            JSONObject value = (JSONObject) listObject.get(0);
//            Globals.token = value.getString("token");
//            imgLink = value.getString("image");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}