package com.example.mahes_000.moviesapp_udacity.moviedata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieReviews;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVReviews;

/**
 * Created by Mahesh Babu Gorantla on 7/6/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // If need to change the database schema, Increment the Number here
    // The Database Version typically starts at version '1', and must be manually incremented each time we release a new APK.
    private static final int DATABASE_VERSION = 4;

    public static final String DATABASE_NAME = "moviestv.db";

    public MovieDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    // This gets called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_ENTRY = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                // This will be used as a Primary Key for Movie Reviews Table
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
//                MovieEntry.COLUMN_GENRES + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_FAVORITES + " INTEGER NOT NULL);";

        final String SQL_CREATE_MOVIE_REVIEWS = "CREATE TABLE " + MovieReviews.TABLE_NAME + " (" +

                MovieReviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                MovieReviews.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieReviews.COLUMN_REVIEW + " TEXT NOT NULL, " +

                // Set the Movie ID Column as a foreign key from MovieEntry table
                " FOREIGN KEY (" + MovieReviews.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "));";

        final String SQL_CREATE_TV_ENTRY = "CREATE TABLE " + TVEntry.TABLE_NAME + " (" +

                TVEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                // This will be used as a Primary key for TV Reviews Table
                TVEntry.COLUMN_TV_ID + " INTEGER NOT NULL UNIQUE, " +
                TVEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                TVEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                TVEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TVEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                TVEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                TVEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                TVEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
//                TVEntry.COLUMN_GENRES + " TEXT NOT NULL, " +
                TVEntry.COLUMN_FAVORITES + " INTEGER NOT NULL);";

        final String SQL_CREATE_TV_REVIEW = "CREATE TABLE " + TVReviews.TABLE_NAME + " (" +

                TVReviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                TVReviews.COLUMN_TV_ID + " INTEGER NOT NULL, " +
                TVReviews.COLUMN_REVIEW + " TEXT NOT NULL, " +

                // Set the Movie ID Column as a foreign key from MovieEntry table
                " FOREIGN KEY (" + TVReviews.COLUMN_TV_ID + ") REFERENCES " +
                TVEntry.TABLE_NAME + " (" + TVEntry.COLUMN_TV_ID + "));";


        // Executing the SQL Commands
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_ENTRY);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_REVIEWS);
        sqLiteDatabase.execSQL(SQL_CREATE_TV_ENTRY);
        sqLiteDatabase.execSQL(SQL_CREATE_TV_REVIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        /*
            This database acts a cache for online data source to prevent the data requests from the
            internet for the same data again. So its upgrade policy is to simply discard the existing
            data and update the data with new data.

            REMEMBER: This onUpgrade Function will only be called when there is a change in the version
                      number of the database
         */

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieReviews.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TVEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TVReviews.TABLE_NAME);

        // Recreating all the tables with the new schema.
        onCreate(sqLiteDatabase);
    }
}
