package edu.miami.c11173414.smartcommunitydrawer;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by conormurray on 4/7/17.
 */

public class JsonReader extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            JSONArray json = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONArray sortJSONbyVotes(JSONArray jsonIn){
        JSONArray jsonArr = jsonIn;
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                jsonValues.add(jsonArr.getJSONObject(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "votes";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                int scoreA = 0;
                int scoreB = 0;

                try {
                    scoreA = a.getInt(KEY_NAME);
                    scoreB = b.getInt(KEY_NAME);
                }
                catch (JSONException e) {
                    //do something
                }

                return -(scoreA-scoreB);
                //if you want to change the sort order, simply use the following:
                //return -scoreA.compareTo(scoreB);
            }
        });

        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        return sortedJsonArray;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
