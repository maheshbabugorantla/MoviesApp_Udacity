package com.example.mahes_000.moviesapp_udacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mahesh Babu Gorantla on 7/9/2016.
 */
public class Video_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View Video_View = inflater.inflate(R.layout.fragment_videos,container, false);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String intent_text = intent.getStringExtra(Intent.EXTRA_TEXT);
            System.out.println("Movie / TV ID in Videos Fragment " + intent_text.split("=")[7]);
        }

        return Video_View;
    }

}
