package com.example.mahes_000.moviesapp_udacity.moviedata;

/**
 * Created by Mahesh Babu Gorantla on 7/5/2016.
 */

import android.provider.BaseColumns;

public class MovieContract {
    // Using BaseColumns will allows us to assign a Unique ID (_ID) for each row in the database.
    public static final class MovieEntry implements BaseColumns {
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

        // Genre IDs
        public static final String COLUMN_GENRES = "genre_ids";

    }

    public static final class MovieReviews implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "moviereview";

        // Column Names for the Above Table

        // Movie ID
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Review from Each User
        public static final String COLUMN_REVIEW = "review";
    }

    public static final class TVEntry implements BaseColumns {

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

        // Genre IDs
        public static final String COLUMN_GENRES = "genre_ids";
    }

    public static final class TVReviews implements BaseColumns {

        // Table Name
        public static final String TABLE_NAME = "tvreview";

        // This is foreign Key from 'tv_shows' table
        public static final String COLUMN_TV_ID = "tv_id";

        // This is the Review for TV Show
        public static final String COLUMN_REVIEW = "review";
    }
}
