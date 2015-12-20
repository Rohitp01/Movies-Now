package com.example.rohit.moviesnow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    final CharSequence[] items = {" Most Popular ", " Highest Rated ", "Favourites"};
    private final String MOST_POPULAR = "popularity.desc";
    private final String HIGHLY_RATED = "vote_count.desc";
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String VOTE_AVERAGE = "vote_average";
    private final String PLOT_SYNOPSIS = "overview";
    private final String TRAILER="trailer";
    private GridView gridView;
    private String resultJSON = null;
    private String[] imgUrl = new String[20];
    private int[] movieID = new int[20];
    private AlertDialog choice;
    private String FLAG_CURRENT = MOST_POPULAR;
    private JSONArray movieDetails;


    public MainActivityFragment() {}


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getContext().getSharedPreferences("sortby", 0);
        FLAG_CURRENT = prefs.getString("SORTSTATE", MOST_POPULAR);
        Toast.makeText(getContext(),"PP"+FLAG_CURRENT,Toast.LENGTH_LONG).show();

        if(savedInstanceState!=null)
        {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        Toast.makeText(getContext(),"ONCV",Toast.LENGTH_LONG).show();
        ImageAdapter adapter = new ImageAdapter(inflater.getContext(),imgUrl);
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootview.findViewById(R.id.movie_grid);
        gridView.setAdapter(adapter);
        updateView(FLAG_CURRENT);
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(),"OS",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = getContext().getSharedPreferences("sortby", 0).edit();
        editor.putString("SORTSTATE", FLAG_CURRENT);
        editor.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapMenu:
                showChoices();
                break;
        }
        return true;
    }

    private void showChoices() {

        choice = new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Toast.makeText(getContext(),"case 0",Toast.LENGTH_LONG).show();
                                FLAG_CURRENT=MOST_POPULAR;
                                updateView(FLAG_CURRENT);
                                break;
                            case 1:Toast.makeText(getContext(),"case 1",Toast.LENGTH_LONG).show();
                                FLAG_CURRENT=HIGHLY_RATED;
                                updateView(FLAG_CURRENT);
                                break;
                        }
                        choice.dismiss();
                    }
                }).setTitle("Choose")
                .show();
    }


    public void updateView(String type) {
        FLAG_CURRENT = type;
        if (FetchMovie()) {
            ImageAdapter adapter = new ImageAdapter(getContext(),imgUrl);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        JSONObject object = movieDetails.getJSONObject(position);
                        String title = object.getString(TITLE);
                        String poster = "" + object.getString(MOVIE_POSTER);
                        String release_date = object.getString(RELEASE_DATE);
                        String vote = object.getString(VOTE_AVERAGE);
                        String plot = object.getString(PLOT_SYNOPSIS);
                        //String trailer=object.getString(TRAILER);
                        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                        intent.putExtra(TITLE, title);
                        intent.putExtra(MOVIE_POSTER, poster);
                        intent.putExtra(RELEASE_DATE, release_date);
                        intent.putExtra(VOTE_AVERAGE, vote);
                        intent.putExtra(PLOT_SYNOPSIS, plot);
                        //intent.putExtra(TRAILER,trailer);

                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog();
                    }

                }
            });
        } else {
            showErrorDialog();
        }
    }




    private boolean FetchMovie() {
        FetchMovieData networkConnection = new FetchMovieData(getContext());
        try {
            resultJSON = networkConnection.execute(FLAG_CURRENT).get();
            if (resultJSON != null) {
                JSONObject movie = new JSONObject(resultJSON);
                movieDetails = movie.getJSONArray("results");
                for (int i = 0; i < movieDetails.length(); i++) {
                    JSONObject temp_mov = movieDetails.getJSONObject(i);
                    imgUrl[i] = "http://image.tmdb.org/t/p/w185/" + temp_mov.getString("poster_path");
                    movieID[i]=temp_mov.getInt("id");
                }
                return true;
            } else
                return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }


    private void showErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setMessage("Sorry Something Went Wrong.Try again Later!")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}

                }).show();
    }


}
