package com.example.mahes_000.moviesapp_udacity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment()
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        // The Code is below is to make the App work with a web API

        /* 1. First Check for Internet Connectivity if not throw a Toast Notification Requesting the User to check for their Internet Connection.
              REMEMBER: In case there is no internet connection, the app will show stored dummy data for the movies.

           2. i)   If Internet is available, Fetch the Images from the theMovieDB
              ii)  Do any Image Conversion as required,
              iii) Compress the Images based on the image size and set them to the respective IDs of ImageViews

           3. Create a list of URLs to fetch the images from

         */

        // Checking for the Internet Connectivity on the Phone.
        boolean NetworkStatus = isNetworkAvailable();

        // List of all ImageView IDs
        int[] array_ids = {R.id.Option_1, R.id.Option_2, R.id.Option_3, R.id.Option_4, R.id.Option_5, R.id.Option_6, R.id.Option_7, R.id.Option_8, R.id.Option_9, R.id.Option_10, R.id.Option_11, R.id.Option_12};

        ArrayList<ImageView> imageViewArrayList = new ArrayList<>();

        if(NetworkStatus == true)
        {
            // This below loop adds all the ImageViews to the ArrayList
            for (int val : array_ids)
            {
                imageViewArrayList.add((ImageView) rootView.findViewById(val));
                ImageView imageView_Picasso = (ImageView) rootView.findViewById(val);

                // Here I am using getActivity() because I need the context in the Fragment and Activity extends Context.
                Picasso.with(getActivity()).load("http://orig00.deviantart.net/12fd/f/2015/243/0/c/albumcoverfor_benprunty_fragments_sylviaritter_by_faith303-d97uftr.png").resize(110, 110).centerCrop().into(imageView_Picasso);
            }
        }

        // Loading Dummy Images
        else
        {
            // Letting the User know that the App is not connected to internet.

            Toast.makeText(getActivity(), "Please make sure you are connected to a network", Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "Loading Dummy Images", Toast.LENGTH_SHORT).show();

            // This below loop adds all the ImageViews to the ArrayList
            for (int val : array_ids)
            {
                imageViewArrayList.add((ImageView) rootView.findViewById(val));
            }
        }


        // Setting the OnClick Listener for the ImageViews
        for (final ImageView imageView : imageViewArrayList) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Movie_Details = imageView.getTag().toString();
                    Intent toDetailActivity = new Intent(getActivity(), Movie_DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, Movie_Details);
                    startActivity(toDetailActivity);
                }
            });
        }

        return(rootView);
    }

}
