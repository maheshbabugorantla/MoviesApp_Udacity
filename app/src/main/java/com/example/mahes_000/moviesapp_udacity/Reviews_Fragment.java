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
import android.widget.ListView;

import com.example.mahes_000.moviesapp_udacity.Interfaces.FetchReviewData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mahesh Babu Gorantla on 7/9/2016.
 *
 * The Fragment Below is used to display the Reviews of the MoviesApp_Udacity.
 *
 */
public class Reviews_Fragment extends Fragment {

    String Video_Choice = null;

    private static final String LOG_TAG = Reviews_Fragment.class.getSimpleName();

    private boolean isNetworkAvailable() {
        /*
            Here I have to use getActivity() for getSystemService() Function is because the getSystemService() function will require context.
            Hence, using getActivity() will give us the context of the Activity in the Non-Activity Class and also as getActivity() extends Context.
            Calling getActivity() will return the Context of the App.
        */

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View Reviews_View = inflater.inflate(R.layout.fragment_reviews, container, false);

        Intent intent = getActivity().getIntent();
        boolean NetworkStatus = isNetworkAvailable();

        // Setting the Adapter for the ListView
        ArrayList<ReviewItem> reviews = new ArrayList<>();
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviews);

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT) && NetworkStatus)
        {
            String intent_text = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] intent_values = intent_text.split("=");

            String ID = intent_values[7];
            Video_Choice = intent_values[6];

            reviews.clear();
            // Fetching the Data reviews for the Movie/TV Show.
            try
            {
                reviews.addAll(getMovieData(ID, Video_Choice));
            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
            }

        }

        // If there is no network connection
        else
        {
            reviews.clear();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "No Reviews Available, Please Check your network connection");
                jsonObject.put("author", "Anonymous");
                reviews.add(new ReviewItem(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListView reviews_list = (ListView) Reviews_View.findViewById(R.id.reviews_list);
        reviews_list.setAdapter(reviewsAdapter);

        return Reviews_View;
    }

    private ArrayList<ReviewItem> getMovieData(String movie_choice, String video_choice) {

        if (video_choice.equals("movie")) {

            FetchReviewData reviewData = new FetchReviewData(getActivity());

            try {
                return (reviewData.execute(movie_choice, video_choice).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "No Reviews Available yet for this TV Show, Please check back again");
                jsonObject.put("author", "Anonymous");

                ArrayList<ReviewItem> TvReviews = new ArrayList<>();
                TvReviews.add(new ReviewItem(jsonObject));
                return (TvReviews);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
