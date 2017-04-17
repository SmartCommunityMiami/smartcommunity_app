package edu.miami.c11173414.smartcommunitydrawer;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
