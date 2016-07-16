package com.example.mahes_000.moviesapp_udacity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;

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
import java.util.ArrayList;
import java.util.Vector;

/**
 *  Created by Mahesh Babu Gorantla on 7/15/2016.
 *
 *  This Class is used to fetch the Movies Data from the tMDB API and insert them into the DataBase (Movies and Movie Reviews Table).
 */
public class FetchMovieData extends AsyncTask<String, Void, String[]> {

    String Movies_Data = null;
    String Video_Choice = "movie";
    String Movies_Choice = "top_rated";

    private ArrayList<ImageItem> mMoviesGrid = new ArrayList<>();
    private GridViewAdapter MoviesAdapter;
    private final Context mContext;

    private static final String LOG_TAG = FetchMovieData.class.getSimpleName();

    public FetchMovieData(Context context, GridViewAdapter moviesAdapter) {

        MoviesAdapter = moviesAdapter;
        mContext = context;

        // Getting the User Preferences.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Movies_Choice = sharedPreferences.getString(mContext.getString(R.string.pref_rating_choice_key), mContext.getString(R.string.pref_rating_choice_default));
        Video_Choice = sharedPreferences.getString(mContext.getString(R.string.pref_video_choice_key), mContext.getString(R.string.pref_video_choice_default));

        System.out.println("Rating Choice: " + Movies_Choice);
        System.out.println("Videos Choice: " + Video_Choice);
    }

    private boolean DEBUG = true;

    private String[] getJSONData(String json_data)
    {
        // This will be returned when the parsing doesn't work as expected.
        String[] Data_movies = null;

        try {
            // Parsing the JSON Data in the form of a String
            JSONObject reader = new JSONObject(json_data);

            JSONArray results_obj = reader.getJSONArray("results");

            // Insert the new movie information into the database
            Vector<ContentValues> cVVector = new Vector<>(results_obj.length());

            // Initializing the String Array to store the data for the Individual Movies
            Data_movies = new String[results_obj.length()];

            for (int index = 0; index < results_obj.length(); index++) {
                ImageItem imageItem = new ImageItem();

                JSONObject jsonObject = results_obj.getJSONObject(index);

                StringBuilder stringBuilder = new StringBuilder();

                String Poster_Path = jsonObject.getString("poster_path");
                stringBuilder.append(Poster_Path);
                stringBuilder.append("=");

                String Overview = jsonObject.getString("overview");
                stringBuilder.append(Overview);
                stringBuilder.append("=");

                String Title = "";
                if (Video_Choice.equals("movie")) {
                    Title = jsonObject.getString("title");
                } else if (Video_Choice.equals("tv")) {
                    Title = jsonObject.getString("name");
                }

                stringBuilder.append(Title);
                stringBuilder.append("=");

                String Vote_Average = jsonObject.getString("vote_average");
                stringBuilder.append(Vote_Average);
                stringBuilder.append("=");

                String Popularity = jsonObject.getString("popularity");
                stringBuilder.append(Popularity);
                stringBuilder.append("=");

                String Release_Date = "";
                if (Video_Choice.equals("movie")) {
                    Release_Date = jsonObject.getString("release_date");
                } else if (Video_Choice.equals("tv")) {
                    Release_Date = jsonObject.getString("first_air_date");
                }

                stringBuilder.append(Release_Date);
                stringBuilder.append("=");
                imageItem.setTitle(Release_Date.split("-")[0]);


                stringBuilder.append(Video_Choice); // Giving the Video Choice
                stringBuilder.append("=");

                String ID = jsonObject.getString("id");
                stringBuilder.append(ID);
                stringBuilder.append("=");

                stringBuilder.append(jsonObject.getString("backdrop_path"));

                String image_url = "http://image.tmdb.org/t/p/w342/" + jsonObject.getString("poster_path");
                imageItem.setImage(image_url);

                mMoviesGrid.add(imageItem);

                ContentValues movieValues = new ContentValues();

                if (Video_Choice.equals("movie")) {
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, ID);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, Release_Date);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, "120");
                    movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, Title);
                } else if (Video_Choice.equals("tv")) {
                    movieValues.put(MovieContract.TVEntry.COLUMN_TV_ID, ID);
                    movieValues.put(MovieContract.TVEntry.COLUMN_RELEASE_DATE, Release_Date);
                    movieValues.put(MovieContract.TVEntry.COLUMN_RUNTIME, "90");
                    movieValues.put(MovieContract.TVEntry.COLUMN_TITLE, Title);
                }

                movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, Poster_Path);
                movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, Overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, Vote_Average);
                movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, Popularity);
                movieValues.put(MovieContract.MovieEntry.COLUMN_FAVORITES, "0");

                Data_movies[index] = stringBuilder.toString();

                cVVector.add(movieValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                if (Video_Choice.equals("movie")) {
                    mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                } else {
                    mContext.getContentResolver().bulkInsert(MovieContract.TVEntry.CONTENT_URI, cvArray);
                }
            }

            // The below code is used to display what we stored in the bulkInsert method.
            String sortOrder = " ";
            if (Movies_Choice.equals("top_rated")) {
                sortOrder = MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
            } else {
                sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
            }

            Cursor cursor = null;
            // The code below is used to query the database
            if (Video_Choice.equals("movie")) {
                cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, sortOrder);
            } else if (Video_Choice.equals("tv")) {
                cursor = mContext.getContentResolver().query(MovieContract.TVEntry.CONTENT_URI, null, null, null, sortOrder);
            }

            if (cursor != null) {
                cVVector = new Vector<>(cursor.getCount());
            }

            if(cursor.moveToFirst())
            {
                do {
                    ContentValues contentValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor,contentValues);
                    cVVector.add(contentValues);
                }while (cursor.moveToNext());
            }

            Log.d(LOG_TAG, "FetchMovieData Complete. " + cVVector.size() + " Inserted");
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in getJSONData Function");
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

        return(Data_movies);
    }


    @Override
    protected String[] doInBackground(String... urls) {
        return getData(urls[0], urls[1], urls[2]);
    }

    public String[] getData(String Movie_Choice, String Video_Type, String page_no)
    {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
        final String API_Key = BuildConfig.THE_MOVIE_DB_API_KEY;
        final String ID = "api_key";
        final String Page = "page";

        Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(Video_Type).appendPath(Movie_Choice).appendQueryParameter(ID, API_Key).appendQueryParameter(Page,page_no).build();

        String BuiltURL = BuiltUri.toString();

        Log.d("Movie URL", BuiltURL);

        try
        {
            URL web_url = new URL(BuiltURL);

            // Creates a request to TheMovieDB Website and opens the Connection
            httpURLConnection = (HttpURLConnection) web_url.openConnection();
            httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
            httpURLConnection.connect(); // Connecting to the Web Link

            // Read the Input Stream into the String
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            // This happens when there is no data being read from the Opened Connection to Web URL.
            if(inputStream == null)
            {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Now reading the raw JSON Data into BufferReader
            while((line=bufferedReader.readLine())!= null)
            {
                buffer.append(line);
            }

            // This might happen when read from URL becomes unsuccessful or may be the connection might drop in between
            if(buffer.length() == 0)
            {
                return null;
            }

            Movies_Data = buffer.toString();
        }

        // This Exception will occur whenever there is MALFORMED URL
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp MalformedURL:", " Malformed URL Exception Occurred");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp IOException: ", "IO Exception Occurred");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("MoviesApp Exception: "," Unknown Exception Occurred");
        }

        // Here Closing all the Open Network Connections and the BufferedReaders.
        finally
        {
            // Checking to see if the connection to web URL is still Open. If yes, then close it.
            if(httpURLConnection != null)
            {
                httpURLConnection.disconnect();
            }

            if(bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Log.e("MoviesApp IOException: ", " IO Exception occurred while closing the reader");
                }
            }
        }

        try
        {
            return getJSONData(Movies_Data);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] s)
    {
        MoviesAdapter.setGridData(mMoviesGrid);
    }
}
