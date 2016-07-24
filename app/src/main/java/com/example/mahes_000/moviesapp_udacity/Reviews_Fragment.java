package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahes_000.moviesapp_udacity.Adapters.ReviewsAdapter;
import com.example.mahes_000.moviesapp_udacity.DataModels.ReviewItem;
import com.example.mahes_000.moviesapp_udacity.Decorations.DividerItemDecoration;
import com.example.mahes_000.moviesapp_udacity.Decorations.VerticalSpaceItemDecoration;
import com.example.mahes_000.moviesapp_udacity.FetchDataTasks.FetchReviewData;

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
    private Context mContext;

    private static final String LOG_TAG = Reviews_Fragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View Reviews_View = inflater.inflate(R.layout.fragment_reviews, container, false);

        Intent intent = getActivity().getIntent();

        boolean NetworkStatus = Utility.isNetworkAvailable(getActivity());

        mContext = getContext();

        // Setting the Adapter for the ListView
        ArrayList<ReviewItem> reviews = new ArrayList<>();
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(mContext, reviews);

        Video_Choice = Utility.getVideoChoice(mContext);

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT) && NetworkStatus)
        {
            String ID = intent.getStringExtra(Intent.EXTRA_TEXT);

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

        RecyclerView reviews_list = (RecyclerView) Reviews_View.findViewById(R.id.reviews_list);

        reviews_list.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adding the Item Decoration
        reviews_list.addItemDecoration(new VerticalSpaceItemDecoration(48));
        reviews_list.addItemDecoration(new DividerItemDecoration(getActivity()));
        reviews_list.addItemDecoration(new DividerItemDecoration(getActivity()));

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