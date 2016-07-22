package com.example.mahes_000.moviesapp_udacity.Adapters;


import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahes_000.moviesapp_udacity.R;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieReviews;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVReviews;
import com.squareup.picasso.Picasso;

/**
 * Created by mahes_000 on 7/20/2016.
 */
public class MovieCursorAdapter extends CursorAdapter {

    private Context mContext;

    public MovieCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.mContext = context;
    }

    private String convertCursortoData(Cursor cursor)
    {
        // Image Path
        int image_thumb = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
        int release_date = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cursor.getString(release_date))
                .append("=")
                .append(cursor.getString(image_thumb));

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
        /* Write Utility Functions to get the Poster Path and Release Year from the Database */

        /* Make Changes to the Database Contract to store the BackDrop path as well */

        String cursor_value = convertCursortoData(cursor);

        String[] cursor_values = cursor_value.split("=");

        String Image_Path = "http://image.tmdb.org/t/p/w342/" + cursor_values[1];
        String Release_Year = cursor_values[0].split("-")[0];

        Log.i("Cursor Value", cursor_value);

        // Setting the Image ThumbNail
        Picasso.with(mContext).load(Image_Path).fit().into((ImageView) view.findViewById(R.id.imageIcon));

        // Setting the Release Year for the Movie/TV Show
        ((TextView) view.findViewById(R.id.imageText)).setText(Release_Year);

        /* Think it over if this is required Create an OnItemClickListener to send the Movie_ID as an Intent to the Detail Activity */

    }




}