package com.example.mahes_000.moviesapp_udacity.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahes_000.moviesapp_udacity.Interfaces.FetchMovieDataInterface;
import com.example.mahes_000.moviesapp_udacity.R;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Mahesh Babu Gorantla on 7/20/2016.
 *
 * This will be the Cursor Adapter to fetch the Data from the Database
 */
public class MovieCursorAdapter extends CursorAdapter {

    private Context mContext;

    private String Video_Choice = "movie";

/*
    private LinkedHashSet<String> IDs = new LinkedHashSet<>();
*/

    private FetchMovieDataInterface mFetchMovieDataInterface;

    public MovieCursorAdapter(Context context, Cursor cursor, int flags) { //, FetchMovieDataInterface activityContext)
        super(context, cursor, flags);

        this.mContext = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Video_Choice = sharedPreferences.getString(context.getString(R.string.pref_video_choice_key), context.getString(R.string.pref_video_choice_default));

/*
        this.mFetchMovieDataInterface = activityContext;
*/

    }

    private String convertCursortoData(Cursor cursor) {
        // Image Path
        int image_thumb = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);

        int release_date;
        int movie_id = 0;

        if (Video_Choice.equals("movie")) {

            release_date = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
/*
            IDs.add(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
*/
            movie_id = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);

        } else {

            release_date = cursor.getColumnIndex(TVEntry.COLUMN_RELEASE_DATE);
/*
            IDs.add(cursor.getString(cursor.getColumnIndex(TVEntry.COLUMN_TV_ID)));
*/
            movie_id = cursor.getColumnIndex(TVEntry.COLUMN_TV_ID);
        }

/*
        mFetchMovieDataInterface.getIds(IDs);
*/

/*
        Log.i("Inside convertCursor", IDs.toString());
*/

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cursor.getString(release_date))
                .append("=")
                .append(cursor.getString(image_thumb))
                .append("=")
                .append(cursor.getString(movie_id));

        return stringBuilder.toString();
    }

    /* Remember that these views will be reused as required */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);

        return view;
    }

    /* This is where we fill in the Data for each View */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String cursor_value = convertCursortoData(cursor);

        String[] cursor_values = cursor_value.split("=");

        String Image_Path = "http://image.tmdb.org/t/p/w342/" + cursor_values[1];
        String Release_Year = cursor_values[0].split("-")[0];

        // Setting the Image ThumbNail
        Picasso.with(mContext).load(Image_Path).fit().into((ImageView) view.findViewById(R.id.imageIcon));

        // Setting the Release Year for the Movie/TV Show
        ((TextView) view.findViewById(R.id.imageText)).setText(Release_Year);

        ((TextView) view.findViewById(R.id.ID_val)).setText(cursor_values[2]);

    }

/*    public void clearIDsList() {

        if (IDs != null) {
            IDs.clear();
        }
    }*/
}
