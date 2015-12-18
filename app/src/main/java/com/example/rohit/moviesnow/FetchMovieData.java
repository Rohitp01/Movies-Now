package com.example.rohit.moviesnow;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rohit on 18/12/2015.
 */
public class FetchMovieData extends AsyncTask<String,Void,String> {

    private final Context mContext;
    private final String API_KEY = "a6db300325aa8dffa561dcbde6226b24";
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String MovieDBjson = null;

    FetchMovieData(Context c)
    {mContext=c;}


    @Override
    protected String doInBackground(String... params) {
        try {
            String sort = params[0];
            URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=" + sort + "&api_key=" + API_KEY);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();  //stores all data fetched
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            MovieDBjson = buffer.toString();
            //url=new URL("http://api.themoviedb.org/3/movie/11309/videos?api_key=a6db300325aa8dffa561dcbde6226b24");

            Log.d("result", MovieDBjson);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return MovieDBjson;
    }

}

