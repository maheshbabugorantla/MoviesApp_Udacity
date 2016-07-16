package com.example.mahes_000.moviesapp_udacity.moviedata;

/**
 * Created by Mahesh Babu Gorantla on 7/5/2016.
 *
 *  MovieContract.java class is used to provide the Database Contract
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {


    /*
        Content Authority is a name for the entire content provider, similar to the
        relationship between a domain name and its website. A Convenient string to use
        for the content authority is the package name for the app, which is guaranteed
        to be unique on the device
    */
    public static final String CONTENT_AUTHORITY = "com.example.mahes_000.moviesapp_udacity";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use
    // to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MOVIES = "movie";
    public static final String PATH_MOVIE_REVIEWS = "moviereview";
    public static final String PATH_TV = "tvshow";
    public static final String PATH_TV_REVIEWS = "tvreview";


    // Using BaseColumns will allows us to assign a Unique ID (_ID) for each row in the database.
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Android Platform's base MIME Type for a content: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Android Platform's base MIME Type for a content: URI containing a Cursor of a single Item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Table Name
        public static final String TABLE_NAME = "movie";

        // This is the Foreign Key for the Movie Details Table.
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // This is used to Fetch the Poster for the Movie
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // This is used to Fetch the Movie Description
        public static final String COLUMN_OVERVIEW = "overview";

        // This is used to title of the Movie
        public static final String COLUMN_TITLE = "title";

        // Average User Rating
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // Popularity Rating
        public static final String COLUMN_POPULARITY = "popularity";

        // Release Date
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Run Time
        public static final String COLUMN_RUNTIME = "runtime";

/*
        // Genre IDs
        public static final String COLUMN_GENRES = "genre_ids";
*/

        // Favorites Indicator
        public static final String COLUMN_FAVORITES = "favorites";

        // Defining a method to build a URI to find a specific movie by its identifier.
        public static Uri buildMovieUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class MovieReviews implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_REVIEWS).build();

        // Android Platform's base MIME Type for a content: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;

        // Android Platform's base MIME Type for a content: URI containing a Cursor of a single Item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;

        // Table Name
        public static final String TABLE_NAME = "moviereview";

        // Column Names for the Above Table

        // Movie ID
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Review from Each User
        public static final String COLUMN_REVIEW = "review";

        // Defining a method to build a URI to find a specific movie reviews by its identifier.
        public static Uri buildMovieReviewsUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    public static final class TVEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TV).build();

        // Android Platform's base MIME Type for a content: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TV;

        // Android Platform's base MIME Type for a content: URI containing a Cursor of a single Item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TV;

        // Table Name
        public static final String TABLE_NAME = "tvshow";

        // This is the Foreign Key for the TV Show Details Table.
        public static final String COLUMN_TV_ID = "tv_id";

        // This is used to Fetch the Poster for the Movie
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // This is used to Fetch the Movie Description
        public static final String COLUMN_OVERVIEW = "overview";

        // This is used to title of the Movie
        public static final String COLUMN_TITLE = "name";

        // Average User Rating
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // Popularity Rating
        public static final String COLUMN_POPULARITY = "popularity";

        // Release Date
        public static final String COLUMN_RELEASE_DATE = "first_air_date";

        // Run Time
        public static final String COLUMN_RUNTIME = "episode_run_time";

/*
        // Genre IDs
        public static final String COLUMN_GENRES = "genre_ids";
*/

        // Favorites Indicator
        public static final String COLUMN_FAVORITES = "favorites";

        // Defining a method to build a URI to find a specific TV Show by its identifier.
        public static Uri buildTVUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TVReviews implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TV_REVIEWS).build();

        // Android Platform's base MIME Type for a content: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TV_REVIEWS;

        // Android Platform's base MIME Type for a content: URI containing a Cursor of a single Item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TV_REVIEWS;

        // Table Name
        public static final String TABLE_NAME = "tvreview";

        // This is foreign Key from 'tv_shows' table
        public static final String COLUMN_TV_ID = "tv_id";

        // This is the Review for TV Show
        public static final String COLUMN_REVIEW = "review";

        // Defining a method to build a URI to find a specific TV show reviews by its identifier.
        public static Uri buildTVReviewsUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
