package com.example.mahes_000.moviesapp_udacity.moviedata;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by mahes_000 on 7/13/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    // Integer Constants for Movie URI Matcher
    private static final int MOVIE = 100; // Used to fetch or insert all the rows of movie details
    private static final int MOVIE_ID = 101; // Used to fetch a row of single movie details
    private static final int MOVIE_REVIEW = 102; // Used to fetch or insert all the reviews for a movie.

    // Integer Constant for TV URI Matcher
    private static final int TVSHOW = 200; // Used to fetch or insert all the rows of tv details
    private static final int TV_ID = 201; // Used to fetch a row of a single TV Show
    private static final int TV_REVIEW = 202; // Used to fetch or insert all the reviews for a TV Show


    public static UriMatcher buildUriMatcher() {

        String content_authority = MovieContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content_authority, MovieContract.PATH_MOVIES, MOVIE);
        matcher.addURI(content_authority, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
        matcher.addURI(content_authority, MovieContract.PATH_MOVIE_REVIEWS + "/#", MOVIE_REVIEW);
        matcher.addURI(content_authority, MovieContract.PATH_TV, TVSHOW);
        matcher.addURI(content_authority, MovieContract.PATH_TV + "/#", TV_ID);
        matcher.addURI(content_authority, MovieContract.PATH_TV_REVIEWS + "/#", TV_REVIEW);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }


    /*
       In order to query the Database, we will switch based on the matched URI integer
       and query the appropriate Table as necessary.
    */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        Cursor returnCursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                returnCursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MOVIE_ID:
                long movie_id = ContentUris.parseId(uri);
                returnCursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movie_id)}, null, null, sortOrder);

/*
                if(cursor.moveToFirst()) {
                    movie_id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                }

                cursor.close();

                returnCursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movie_id)}, null, null, sortOrder);
*/
                break;

            case MOVIE_REVIEW:
                long movie_review_id = ContentUris.parseId(uri);
                System.out.println("MovieReview Row ID (Main): " + movie_review_id);

                returnCursor = database.query(MovieContract.MovieReviews.TABLE_NAME, projection, MovieContract.MovieReviews.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movie_review_id)}, null, null, sortOrder);

                /*if(cursor.moveToFirst()) {
                    movie_review_id = cursor.getInt(cursor.getColumnIndex("movie_id"));
                }

                cursor.close();

                returnCursor = database.query(MovieContract.MovieReviews.TABLE_NAME, projection, MovieContract.MovieReviews.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movie_review_id)}, null, null, sortOrder);*/
                break;

            case TVSHOW:
                returnCursor = database.query(MovieContract.TVEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case TV_ID:
                long tv_id = ContentUris.parseId(uri);
                returnCursor = database.query(MovieContract.TVEntry.TABLE_NAME, projection, MovieContract.TVEntry.COLUMN_TV_ID + " = ?", new String[]{String.valueOf(tv_id)}, null, null, sortOrder);

/*
                if(cursor.moveToFirst()) {
                    tv_id = cursor.getInt(cursor.getColumnIndex(MovieContract.TVEntry.COLUMN_TV_ID));
                }

                cursor.close();

                returnCursor = database.query(MovieContract.TVEntry.TABLE_NAME, projection, MovieContract.TVEntry.COLUMN_TV_ID + " = ?", new String[]{String.valueOf(tv_id)}, null, null, sortOrder);
*/
                break;

            case TV_REVIEW:
                long tv_review_id = ContentUris.parseId(uri);
                System.out.println("TV Review ID (Main): " + tv_review_id);
                returnCursor = database.query(MovieContract.TVReviews.TABLE_NAME, projection, MovieContract.TVReviews.COLUMN_TV_ID + " = ?", new String[]{String.valueOf(tv_review_id)}, null, null, sortOrder);

/*
                if(cursor.moveToFirst())
                {
                    tv_review_id = cursor.getInt(cursor.getColumnIndex(MovieContract.TVReviews.COLUMN_TV_ID));
                }
                cursor.close();

                returnCursor = database.query(MovieContract.TVReviews.TABLE_NAME, projection, MovieContract.TVReviews.COLUMN_TV_ID + " = ?", new String[]{String.valueOf(tv_review_id)}, null, null, sortOrder);
*/
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        /* This causes the cursor to register the content observer, to watch for changes that
         * happen to the URI and any of its descendants. This allows the content Provider to easily
         * tell the UI when the cursor changes, on operations like a database insert or update.  */
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }


    /* This method is used to find the MIME Type of the results. Either a Directory of zero/more results or a single item */
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            case MOVIE_REVIEW:
                return MovieContract.MovieReviews.CONTENT_TYPE;

            case TVSHOW:
                return MovieContract.TVEntry.CONTENT_TYPE;

            case TV_ID:
                return MovieContract.TVEntry.CONTENT_ITEM_TYPE;

            case TV_REVIEW:
                return MovieContract.TVReviews.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Cannot resolve Unknown Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }

                break;

            case MOVIE_REVIEW:
                _id = database.insert(MovieContract.MovieReviews.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }

                break;

            case TVSHOW:
                _id = database.insert(MovieContract.TVEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }

                break;

            case TV_REVIEW:
                _id = database.insert(MovieContract.TVReviews.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = MovieContract.TVReviews.buildTVReviewsUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    // Both Delete and Update methods below take the "selection" string and arguments to define which rows should be deleted or updated
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int rows = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                rows = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_REVIEW:
                rows = database.delete(MovieContract.MovieReviews.TABLE_NAME, selection, selectionArgs);
                break;

            case TVSHOW:
                rows = database.delete(MovieContract.TVEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TV_REVIEW:
                rows = database.delete(MovieContract.TVReviews.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        // If selection is null then it will delete all the rows in the Table
        // And only if the value of rows is greater than Zero we need to notify the changes to the ContentResolver
        if (selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int rows;
        long _id;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                rows = database.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MOVIE_REVIEW:
                _id = ContentUris.parseId(uri);
                rows = database.update(MovieContract.MovieReviews.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TVSHOW:
                rows = database.update(MovieContract.TVEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TV_REVIEW:
                rows = database.update(MovieContract.TVReviews.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    /* Putting a bunch of interests into a single transaction is much faster than inserting each row individually.
     *
     * BulkInsert function will allow us to do that work in a single Transaction
     *
     *  NOTE: If we do not set the transaction to be successful the records will not be committed when we call
     *        endTransaction.
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int returnCount = 0;

        switch (match) {
            case MOVIE:
                database.beginTransaction();

                try {

                    for (ContentValues value : values) {

                        long _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }

                    database.setTransactionSuccessful();
                }

                finally {
                    database.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case MOVIE_REVIEW:
                database.beginTransaction();

                try {

                    for (ContentValues value : values) {

                        long _id = database.insert(MovieContract.MovieReviews.TABLE_NAME, null, value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }

                    database.setTransactionSuccessful();
                }

                finally {
                    database.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case TVSHOW:
                database.beginTransaction();

                try {

                    for (ContentValues value : values) {

                        long _id = database.insert(MovieContract.TVEntry.TABLE_NAME, null, value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }

                    database.setTransactionSuccessful();
                }

                finally {
                    database.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case TV_REVIEW:
                database.beginTransaction();

                try {

                    for (ContentValues value : values) {

                        long _id = database.insert(MovieContract.TVReviews.TABLE_NAME, null, value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }

                    database.setTransactionSuccessful();
                }

                finally {
                    database.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
