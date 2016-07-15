package com.example.mahes_000.moviesapp_udacity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieDBHelper;
import com.example.mahes_000.moviesapp_udacity.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by mahes_000 on 7/6/2016.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, index == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'." + error, expectedValue, valueCursor.getString(index));
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

    static ContentValues createMovieReviews(long MovieRowId) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieReviews.COLUMN_MOVIE_ID, MovieRowId);
        contentValues.put(MovieContract.MovieReviews.COLUMN_REVIEW, "Its a good video");

        return contentValues;
    }

    static ContentValues createTVEntry() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.TVEntry.COLUMN_TV_ID, 12345);
        contentValues.put(MovieContract.TVEntry.COLUMN_POSTER_PATH, "/blahblah.jpg");
        contentValues.put(MovieContract.TVEntry.COLUMN_OVERVIEW, "Blah Blah Nothing");
        contentValues.put(MovieContract.TVEntry.COLUMN_TITLE, "Blah Blah");
        contentValues.put(MovieContract.TVEntry.COLUMN_VOTE_AVERAGE, 8.4);
        contentValues.put(MovieContract.TVEntry.COLUMN_POPULARITY, 8.5);
        contentValues.put(MovieContract.TVEntry.COLUMN_RELEASE_DATE, "N/A");
        contentValues.put(MovieContract.TVEntry.COLUMN_RUNTIME, "120");
        contentValues.put(MovieContract.TVEntry.COLUMN_GENRES, "1234,1235,1236");
        contentValues.put(MovieContract.TVEntry.COLUMN_FAVORITES, 1);

        return contentValues;
    }

    static ContentValues createTVReviews(long tv_id) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.TVReviews.COLUMN_TV_ID, tv_id);
        contentValues.put(MovieContract.TVReviews.COLUMN_REVIEW, "Its a good video");

        return contentValues;
    }


    static long insertMovieEntryValues(Context context) {
        // Insert our test Records into the database
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createMovieEntry();

        long MovieRowId;
        MovieRowId = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);

        // Verify that we got a row back i.e. We inserted a Row
        assertTrue("Error: Failure to insert MovieEntry Values into the MovieEntry Table ", MovieRowId != -1);

        return MovieRowId;
    }

    static long insertMovieReviewValues(Context context)
    {
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createMovieReviews(12345);

        long MovieReviewId;
        MovieReviewId = database.insert(MovieContract.MovieReviews.TABLE_NAME, null, contentValues);

        assertTrue("Error: Failure to insert MovieReview Values into the MovieReview Table ", MovieReviewId != -1);

        return MovieReviewId;
    }

    static long insertTVEntryValues(Context context) {
        // Insert our test Records into the database
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createTVEntry();

        long TVEntry_Id;
        TVEntry_Id = sqLiteDatabase.insert(MovieContract.TVEntry.TABLE_NAME, null, contentValues);

        // Verify that we got a row back i.e. We inserted a Row
        assertTrue("Error: Failure to insert TVEntry Values into the TVEntry Table ", TVEntry_Id != -1);

        return TVEntry_Id;
    }

    static long insertTVReviewValues(Context context)
    {
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createTVReviews(12345);

        long TVReviewId;
        TVReviewId = database.insert(MovieContract.TVReviews.TABLE_NAME, null, contentValues);

        assertTrue("Error: Failure to insert TVReview Values into the TVReview Table ", TVReviewId != -1);

        return TVReviewId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}

