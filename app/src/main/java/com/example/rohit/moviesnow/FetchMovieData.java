package com.example.rohit.moviesnow;

import android.content.Context;
import android.net.Uri;
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
    private final String API_KEY = "ENTER THE KEY HERE";
    public static final String TRAILER_QUERY_KEY = "videos";
    public static final String REVIEW_QUERY_KEY = "reviews";
    private boolean trailerQuery;
    private boolean reviewQuery;
    private boolean movieQuery;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String MovieDBjson = null;

    FetchMovieData(Context c)
    {mContext=c;}


    @Override
    protected String doInBackground(String... params) {
        try {
            String sort = "sort_by";
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String API = "api_key";
            Uri builtUri;


        /* if(params.length > 1){
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(String.valueOf(params[0]))
                        .appendPath(String.valueOf(params[1]))
                        .appendQueryParameter(API,API_KEY )
                        .build();

                if(String.valueOf(params[1]).equals(TRAILER_QUERY_KEY)){
                    trailerQuery = true;
                }
                if(String.valueOf(params[1]).equals(REVIEW_QUERY_KEY)){
                    reviewQuery = true;
                }
            }else {*/
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(sort, params[0])
                        .appendQueryParameter(API, API_KEY)
                        .build();
                movieQuery = true;
           // }
            URL url = new URL(builtUri.toString());


           // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=" + sort + "&api_key=" + API_KEY);
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
            //url=new URL("http://api.themoviedb.org/3/movie/11309/videos?api_key=");
            //"http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
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

