package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_DetailsActivityFragment extends Fragment
{

    public Movie_DetailsActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    private boolean isNetworkAvailable()
    {
        /*
            Here I have to use getActivity() for getSystemService() Function is because the getSystemService() function will require context.
            Hence, using getActivity() will give us the context of the Activity in the Non-Activity Class and also as getActivity() extends Context.
            Calling getActivity() will return the Context of the App.
        */

        ConnectivityManager connectivityManager =  (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Creating a rootView
        View rootView = inflater.inflate(R.layout.fragment_movie__details, container, false);

        Intent intent = getActivity().getIntent();

        boolean networkStatus = isNetworkAvailable();

//        boolean flag_data_updated = false;

        if((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && networkStatus)
        {
//            flag_data_updated = true;

            String text_detail = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] str_values = text_detail.split("=");

            String Image_Base_URL = "http://image.tmdb.org/t/p/w185/";
            String Image_URL = Image_Base_URL + str_values[0];

            // Setting the Image for the Poster
            Picasso.with(getActivity()).load(Image_URL).fit().into((ImageView) rootView.findViewById(R.id.details_poster));

            ((TextView) rootView.findViewById(R.id.movie_title)).setText(str_values[2]); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText(str_values[1]); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText(str_values[3] + "/10"); // User Rating Value

            ((TextView) rootView.findViewById(R.id.release_date_value)).setText(str_values[5].split("-")[0]);  // Release Date (Movie) or First Aired Date (TV)

        }

        else if((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && !networkStatus)
        {
            // Setting the Image
            ImageView imageView = (ImageView) rootView.findViewById(R.id.details_poster);
            imageView.setImageResource(R.drawable.movie1);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText("Blah Blah"); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText("Blah Blah"); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText("0.0" + " / 10");

            ((TextView) rootView.findViewById(R.id.release_date_value)).setText("N/A");
        }

        return(rootView);
    }

}
