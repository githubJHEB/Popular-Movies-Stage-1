package com.example.android.moviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import android.content.Context;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Transformation;

import static android.R.attr.format;
import static android.R.attr.fragment;
import static com.example.android.moviesapp.R.id.container;

/**
 * Created by jman on 20/09/16.
 */

public class MoviesFragment extends Fragment {
    private static final String DEGBUG_TA = "TRAK";
    ImageAdapter mMoviesAdapter;
    int densityDpiGridViewW;
    int densityDpiGridViewH;
    private String moviesStrDataP;
    private String[] moviesStrDataDetail;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        //Inflate the content of the menu layout
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Checks if the settings option menu was selected, and then initilize a settings Activity, to save the option selected.
                Intent settingactivity = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingactivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //Notify that MoviesFragment has a menu

        //Define he input parameter for gridview layout about height and width
        int densityDpi = metrics.densityDpi;
        if (densityDpi >= 240 && densityDpi < 320) {
            densityDpiGridViewW = 140;
            densityDpiGridViewH = 210;
        } else if (densityDpi >= 320 && densityDpi < 480) {
            densityDpiGridViewW = 220;
            densityDpiGridViewH = 300;
        } else if (densityDpi > -480 && densityDpi < 640) {
            densityDpiGridViewW = 405;
            densityDpiGridViewH = 575;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        mMoviesAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(mMoviesAdapter);
        //Bind the adpter, to GridView and data.
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = moviesStrDataDetail[position];
                Intent parametersIntent = new Intent(getContext(), Detail_Activity.class);
                //Sends a string whit all information need for Detail Activity.
                parametersIntent.putExtra("moviesStrDataP", data);
                startActivity(parametersIntent);
            }
        });

        return rootView;
    }

    private String[] getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {
        //Obtains from JSON all the information necessary to see in the main and detail fragment. Adds labels to separate the information
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_SYPNOPSIS = "overview";

        String resultStrsPoster;
        String resultStrTitle;
        String resultStrsReleaseDate;
        String resultStrsVoteAverage;
        String resultStrsOverview;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        String[] resultStrs = new String[moviesArray.length()];
        if (moviesArray.length() != 0) {

            for (int i = 0; i < moviesArray.length(); ++i) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                resultStrsPoster = movieObject.getString(TMDB_POSTER).trim();
                resultStrTitle = movieObject.getString(TMDB_TITLE);
                resultStrsReleaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                resultStrsVoteAverage = movieObject.getString(TMDB_VOTE_AVERAGE);
                resultStrsOverview = movieObject.getString(TMDB_SYPNOPSIS);
                resultStrs[i] = "#0##" + "http://image.tmdb.org/t/p/" + "w185" + resultStrsPoster + "#1##" + resultStrTitle + "#2##" + resultStrsReleaseDate + "#3##"
                        + resultStrsVoteAverage + "#4##" + resultStrsOverview + "#5##";
            }

        }
        return resultStrs;
    }

    private void updateMovies() {
        //Procedure to update the information on the Generic View (GridView) and DetailView
        FetchMoviesTask weatherTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitType = prefs.getString(getString(R.string.pref_Order_of_the_movies_key), getString(R.string.pref_Order_of_the_movies_Most_Popular));
        weatherTask.execute(unitType);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
        //Async task with short duration to separate work of the main treat to a secon second treat, to obtain and format to the information from Picasso and TheMovies.org        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private Context mContext;
        private String moviesStrData;

        @Override
        //Modify the adpter with the new information requested by the menu option. (Most Popular, an The height rated
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                mMoviesAdapter.clear();
                for (int i = 0; i < strings.length; ++i) {
                    moviesStrDataP = strings[i];
                    moviesStrDataDetail = strings;
                    moviesStrData = moviesStrDataP.substring(moviesStrDataP.indexOf("#0##") + 4, moviesStrDataP.indexOf("#1##"));
                    mMoviesAdapter.add(moviesStrData);
                }
                mMoviesAdapter.notifyDataSetChanged();
            }
        }


        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr;

            String apiKey = BuildConfig.OPEN_MOVIES_MAP_API_KEY;

            try {

                String PARAM = "sort_by";
                // Construct the URL for the TheMovieDb query
                // Possible parameters are available at TheMovieDb API page
                String parametro = params[0];
                if ("highestrated".equals(parametro)) {
                    PARAM = "top_rated";
                } else {
                    PARAM = "popular";
                }
                final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/" + PARAM + "?";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();
                URL url = new URL(builtUri.toString());
                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            try {
                //Extrac a String [] whith the information necesary
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                //Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        //Custom adapter, that return the image View necessary to bind with the gridView
        List<String> list = new ArrayList<>();
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public void add(String s) {
            list.add(s);
        }

        public void clear() {
            list.clear();
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(densityDpiGridViewW, densityDpiGridViewH));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            Picasso
                    .with(mContext)
                    .load(list.get(position))
                    .placeholder(R.drawable.timer)
                    .fit()
                    .into(imageView);
            return imageView;

        }
    }

}