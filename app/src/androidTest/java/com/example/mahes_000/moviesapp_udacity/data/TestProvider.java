package com.example.mahes_000.moviesapp_udacity.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.MovieReviews;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVEntry;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract.TVReviews;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieDBHelper;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieProvider;

/**
 * Created by mahes_000 on 7/14/2016.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();


    public void deleteAllRecordsFromProvider() {

        // Movie Details Table
        mContext.getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);

        // Movie Reviews Table
        mContext.getContentResolver().delete(MovieReviews.CONTENT_URI, null, null);

        // TV Show Details Table
        mContext.getContentResolver().delete(TVEntry.CONTENT_URI, null, null);

        // TV Show Reviews Table
        mContext.getContentResolver().delete(TVReviews.CONTENT_URI, null, null);

        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from MovieEntry Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(MovieReviews.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from the MovieReviews Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(TVEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from MovieEntry Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(TVReviews.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from the MovieReviews Table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB()
    {
        MovieDBHelper movieDBHelper = new MovieDBHelper(mContext);
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();

        database.delete(MovieEntry.TABLE_NAME, null, null);
        database.delete(MovieReviews.TABLE_NAME, null, null);
        database.delete(TVEntry.TABLE_NAME, null, null);
        database.delete(TVReviews.TABLE_NAME, null, null);

        // Closing the database to free up the space.
        database.close();
    }

    public void deleteAllRecords()
    {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    // This is used to check if the MovieProvider is registered correctly
    public void testProviderRegistry()
    {
        PackageManager packageManager = mContext.getPackageManager();

        /*
            We define the component name based on the package name from the context and the
            MovieProvider Class
        */
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MovieProvider.class.getName());

        try{

            /*
            * Fetch the provider info using the component name from PackageManager
            * This throws an exception if the provider isn't registered
            * */
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);

            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);

        }
        catch(PackageManager.NameNotFoundException e)
        {
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    // This test doesn't involve the Database
    public void testGetType()
    {
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);

        assertEquals("Error: The MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieEntry.CONTENT_TYPE, type);

        // Movie Entry Table
        long test_ID = 201229; // Sample Row ID

        // content://CONTENT_AUTHORITY/movie/test_ID
        type = mContext.getContentResolver().getType(MovieEntry.buildMovieUri(test_ID));

        // This is used to check if the getType will return the CONTENT_URI to fetch a single row in the MovieEntry Table.
        assertEquals("Error: the MovieEntry CONTENT_URI with row ID should return MovieEntry.CONTENT_ITEM_TYPE", MovieEntry.CONTENT_ITEM_TYPE, type);


        // Movie Reviews Table
        long test_Review_ID = 201229; // Sample Row ID for a Row in Reviews Table
        type = mContext.getContentResolver().getType(MovieReviews.buildMovieReviewsUri(test_Review_ID));
        assertEquals("Error: the MovieReviews CONTENT_URI with row ID should return MovieReviews.CONTENT_TYPE", MovieReviews.CONTENT_TYPE, type);

        // TV Entry Table
        long test_TV_ID = 000000;
        type = mContext.getContentResolver().getType(TVEntry.buildTVUri(test_TV_ID));
        assertEquals("Error: the TVEntry CONTENT_URI with row ID should return TVEntry.CONTENT_ITEM_TYPE", TVEntry.CONTENT_ITEM_TYPE, type);

        // TV Reviews Table
        long test_TV_Reviews = 000000;
        type = mContext.getContentResolver().getType(TVReviews.buildTVReviewsUri(test_TV_Reviews));
        assertEquals("Error: the TVReviews CONTENT_URI with row ID should return TVReviews.CONTENT_TYPE", TVReviews.CONTENT_TYPE, type);
    }

        /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicMovieQuery() {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createMovieEntry();
        long MovieRowId = TestUtilities.insertMovieEntryValues(mContext);

        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, MovieContract.MovieEntry._ID + " = ?", new String[]{Long.toString(MovieRowId)}, null, null, null);

        int movie_id = -1;

        if(cursor.moveToFirst())
        {
            movie_id = cursor.getInt(cursor.getColumnIndex("movie_id"));
        }

        // Validate data in resulting Cursor with the Original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Movie Entry Query Validation Failed", cursor, movieValues);
        cursor.close();

        ContentValues reviewValues = TestUtilities.createMovieReviews(movie_id);
        long ReviewRowId = db.insert(MovieReviews.TABLE_NAME, null, reviewValues);
        assertTrue("Unable to Insert MovieReview into the Database", ReviewRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor MovieCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );


        Cursor MovieReviewCursor = mContext.getContentResolver().query(
                Uri.parse(MovieReviews.CONTENT_TYPE),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherQuery", MovieCursor, movieValues);
        //TestUtilities.validateCursor("testBasicWeatherQuery", MovieReviewCursor, reviewValues);
    }

    public void testBasicTVQuery()
    {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues TVValues = TestUtilities.createTVEntry();
        long TVRowId = TestUtilities.insertTVEntryValues(mContext);

        Cursor cursor = db.query(TVEntry.TABLE_NAME, null, TVEntry._ID + " = ?", new String[]{Long.toString(TVRowId)}, null, null, null);

        int tv_id = -1;

        if(cursor.moveToFirst())
        {
            tv_id = cursor.getInt(cursor.getColumnIndex("tv_id"));
        }

        // Validate data in resulting Cursor with the Original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Movie Entry Query Validation Failed", cursor, TVValues);
        cursor.close();

        ContentValues reviewValues = TestUtilities.createTVReviews(tv_id);
        long ReviewRowId = db.insert(TVReviews.TABLE_NAME, null, reviewValues);
        assertTrue("Unable to Insert MovieReview into the Database", ReviewRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor TVCursor = mContext.getContentResolver().query(
                TVEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Cursor TVReviewCursor = mContext.getContentResolver().query(
                Uri.parse(TVReviews.CONTENT_TYPE),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherQuery", TVCursor, TVValues);
 //       TestUtilities.validateCursor("testBasicWeatherQuery", TVReviewCursor, reviewValues);
    }

     /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    public void testUpdateMovieEntry() {

        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMovieReviews(12345);

        Uri reviewUri = mContext.getContentResolver().
                insert(MovieReviews.CONTENT_URI, values);
        long reviewRowId = ContentUris.parseId(reviewUri);

        // Verify we got a row back.
        assertTrue(reviewRowId != -1);
        Log.d(LOG_TAG, "New row id: " + reviewRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieReviews._ID, reviewRowId);
        updatedValues.put(MovieReviews.COLUMN_REVIEW, "Santa's Village");
        updatedValues.put(MovieReviews.COLUMN_MOVIE_ID, 12345);

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor locationCursor = mContext.getContentResolver().query(MovieReviews.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        locationCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieReviews.CONTENT_URI, updatedValues, MovieReviews._ID + "= ?",
                new String[] { Long.toString(reviewRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        locationCursor.unregisterContentObserver(tco);
        locationCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieReviews.CONTENT_URI,
                null,   // projection
                MovieReviews._ID + " = " + reviewRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    /* This tears down the test */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


}
