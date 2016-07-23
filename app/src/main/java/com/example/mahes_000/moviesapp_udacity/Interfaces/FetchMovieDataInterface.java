package com.example.mahes_000.moviesapp_udacity.Interfaces;

import com.example.mahes_000.moviesapp_udacity.DataModels.ImageItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by mahes_000 on 7/16/2016.
 */
public interface FetchMovieDataInterface {

        void onDownloadComplete(ArrayList<ImageItem> gridData);

        void onDownloadReviews();

        void getIds(LinkedHashSet<String> Movie_IDs);
}
