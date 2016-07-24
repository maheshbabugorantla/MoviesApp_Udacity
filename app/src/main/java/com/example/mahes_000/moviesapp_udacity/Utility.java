package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by Mahesh Babu Gorantla on 7/24/2016.
 *
 * This Class Contains Methods to are Generic to most of the Activities/Fragments in this Project
 */
public class Utility {

    public static boolean isNetworkAvailable(Context context) {
        /*
            Here I have to use getActivity() for getSystemService() Function is because the getSystemService() function will require context.
            Hence, using getActivity() will give us the context of the Activity in the Non-Activity Class and also as getActivity() extends Context.
            Calling getActivity() will return the Context of the App.
        */

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static String getRatingChoice(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return(sharedPreferences.getString(context.getString(R.string.pref_rating_choice_key), context.getString(R.string.pref_rating_choice_default)));
    }

    public static String getVideoChoice(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (sharedPreferences.getString(context.getString(R.string.pref_video_choice_key), context.getString(R.string.pref_video_choice_default)));
    }
}
