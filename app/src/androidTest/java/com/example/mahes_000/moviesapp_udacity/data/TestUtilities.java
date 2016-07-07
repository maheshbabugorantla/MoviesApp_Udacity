package com.example.mahes_000.moviesapp_udacity.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;

import java.util.Map;
import java.util.Set;

/**
 * Created by mahes_000 on 7/6/2016.
 */
public class TestUtilities extends AndroidTestCase{

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues)
    {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String, Object> entry: valueSet)
        {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, index == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'." + error, expectedValue,valueCursor.getString(index));
        }
    }

    static ContentValues createMovieEntry() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 12345);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/blahblah.jpg");
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Blah Blah Nothing");
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Blah Blah");
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 8.4);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 8.5);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "N/A");
        contentValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, "120");
        contentValues.put(MovieContract.MovieEntry.COLUMN_GENRES, "1234,1235,1236");
        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITES, 1);

        return contentValues;
    }

    static ContentValues createMovieReviews() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieReviews.COLUMN_MOVIE_ID, 12345);
        contentValues.put(MovieContract.MovieReviews.COLUMN_REVIEW, "Its a good video");

        return contentValues;
    }



}
