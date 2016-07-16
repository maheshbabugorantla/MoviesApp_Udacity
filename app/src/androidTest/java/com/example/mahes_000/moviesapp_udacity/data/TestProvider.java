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
import java.util.Random;


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
        mContext.getContentResolver().delete(MovieReviews.buildMovieReviewsUri(9), null, null);

        // TV Show Details Table
        mContext.getContentResolver().delete(TVEntry.CONTENT_URI, null, null);

        // TV Show Reviews Table
        mContext.getContentResolver().delete(TVReviews.buildTVReviewsUri(9), null, null);

        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from MovieEntry Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(MovieReviews.buildMovieReviewsUri(9), null, null, null, null);
        assertEquals("Error: Records not deleted from the MovieReviews Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(TVEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from MovieEntry Table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(TVReviews.buildTVReviewsUri(9), null, null, null, null);
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
        System.out.println("Movie Review Row ID (Test): " + ReviewRowId);
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
                MovieReviews.buildMovieReviewsUri(ReviewRowId),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("test Basic MovieEntry Query", MovieCursor, movieValues);
        TestUtilities.validateCursor("testBasic MovieReview Query", MovieReviewCursor, reviewValues);
    }

    public void testBasicTVQuery()
    {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues TVValues = TestUtilities.createTVEntry();
        long TVRowId = TestUtilities.insertTVEntryValues(mContext);

        assertTrue("Unable to Insert TV Review into TVReviews Table", TVRowId != -1);

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
        System.out.println("Test TV Review values: " + reviewValues);
        long ReviewRowId = db.insert(TVReviews.TABLE_NAME, null, reviewValues);
        System.out.println("TV Review Row ID (Test): " + ReviewRowId);
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
                TVReviews.buildTVReviewsUri(ReviewRowId),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("test BasicTVEntry Query ", TVCursor, TVValues);
        TestUtilities.validateCursor("test BasicTVReview Query ", TVReviewCursor, reviewValues);
    }

     /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    public void testUpdateMovieReview() {

        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMovieReviews(12345);

        Uri reviewUri = mContext.getContentResolver().
                insert(MovieReviews.buildMovieReviewsUri(9), values);
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
        Cursor reviewCursor = mContext.getContentResolver().query(MovieReviews.buildMovieReviewsUri(reviewRowId), null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        reviewCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieReviews.buildMovieReviewsUri(reviewRowId), updatedValues, MovieReviews._ID + "= ?",
                new String[]{Long.toString(reviewRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        reviewCursor.unregisterContentObserver(tco);
        reviewCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieReviews.buildMovieReviewsUri(reviewRowId),
                null,   // projection
                MovieReviews._ID + " = " + reviewRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createMovieReviews(12345);

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieReviews.CONTENT_URI, true, tco);
        Uri reviewUri = mContext.getContentResolver().insert(MovieReviews.buildMovieReviewsUri(9), testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(reviewUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieReviews.buildMovieReviewsUri(locationRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestUtilities.createMovieEntry();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(MovieEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
                weatherCursor, weatherValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        weatherValues.putAll(testValues);

    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, movieObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver movieReviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieReviews.CONTENT_URI, true, movieReviewObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieObserver.waitForNotificationOrFail();
        movieReviewObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(movieReviewObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertMovieValues()
    {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        String[] title = {"Mahesh Babu", "Bhaskar Addala", "Venu Babu", "Sarvani", "Ajay Babu", "Mahesh Babu1", "Bhaskar Addala1", "Venu Babu1", "Sarvani1", "Ajay Babu1"};

        int movie_ID = 12345;

        for(int index = 0; index < BULK_INSERT_RECORDS_TO_INSERT; index++)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_ID);
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/blahblah.jpg");
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Blah Blah Nothing");
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title[index]);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, randInt(5, 10));
            contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, randInt(5, 10));
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "N/A");
            contentValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, "120");
//            contentValues.put(MovieContract.MovieEntry.COLUMN_GENRES, "1234,1235,1236");
            contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITES, 1);

            movie_ID += 1;

            returnContentValues[index] = contentValues;
        }

        return returnContentValues;
    }

    static ContentValues[] createBulkReviewValues(long Movie_ID)
    {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for(int index = 0; index < BULK_INSERT_RECORDS_TO_INSERT; index++)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieReviews.COLUMN_MOVIE_ID, Movie_ID);
            contentValues.put(MovieReviews.COLUMN_REVIEW, "Blah Blah");

            returnContentValues[index] = contentValues;
        }

        return returnContentValues;
    }

    public static int randInt(int min, int max)
    {
        Random random = new Random();

        int randomNumber = random.nextInt((max - min) + 1) + min;

        return randomNumber;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();

        // Register a content observer for our Movie bulk insert.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, movieObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }

        bulkInsertContentValues = createBulkReviewValues(12345);

        // Register a content observer for our MovieReviews bulk insert.
        TestUtilities.TestContentObserver movieReviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieReviews.CONTENT_URI, true, movieReviewObserver);

        insertCount = mContext.getContentResolver().bulkInsert(MovieReviews.buildMovieReviewsUri(12345), bulkInsertContentValues);

        MovieDBHelper movieDBHelper = new MovieDBHelper(mContext);
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();

        cursor = database.query(MovieReviews.TABLE_NAME, null, MovieReviews.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(12345)},null, null, null);

        long row_ID = -1;

        if(cursor.moveToFirst())
        {
            row_ID = cursor.getLong(cursor.getColumnIndex("_id"));
        }

        assertTrue("Error: No Rows Available in the MovieReviews Table", row_ID != -1);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        movieReviewObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieReviewObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                MovieReviews.buildMovieReviewsUri(row_ID),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }

        cursor.close();
        database.close();
    }

    /* This tears down the test */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


}
