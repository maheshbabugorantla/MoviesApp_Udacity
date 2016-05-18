package com.example.mahes_000.moviesapp_udacity;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_DetailsActivityFragment extends Fragment
{

    public Movie_DetailsActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Creating a rootView
        View rootView = inflater.inflate(R.layout.fragment_movie__details, container, false);

        Intent intent = getActivity().getIntent();

        if((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String text_detail = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] str_values = text_detail.split("=");

            ((TextView) rootView.findViewById(R.id.movie_title)).setText(str_values[2]); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText(str_values[1]); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText(str_values[3] + " / 10");
        }

        return(rootView);
    }
}
