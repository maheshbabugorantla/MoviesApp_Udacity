package com.example.mahes_000.moviesapp_udacity.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieDBHelper;

import java.util.HashSet;

/**
 * Created by mahes_000 on 7/6/2016.
 */
public class TestDb extends AndroidTestCase {

    void deleteDatabase()
    {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        return;
    }


    // Start each test with a clean database.
    @Override
    protected void setUp() throws Exception
    {
        deleteDatabase();
    }

    public void testCreateDb() throws Throwable{

        // Build a HashSet of all the table name we wish to look for
        // Not that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.MovieReviews.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TVEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TVReviews.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);

        SQLiteDatabase sqLiteDatabase = new MovieDBHelper(this.mContext).getWritableDatabase();

        // This confirms that the database is open.
        assertEquals(true, sqLiteDatabase.isOpen());

        // Have we created the tables we want ?
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", cursor.moveToFirst());

        // Verify that the tables have been created
        do {
                tableNameHashSet.remove(cursor.getString(0));
           } while(cursor.moveToNext());

        // If this fails, it means that the database doesn't contain Movie/TV Entry
        // and Movie/TV reviews Tables
        assertTrue("Error: The Database was created without MovieEntry/TVEntry and MovieReview/TVReview tables", tableNameHashSet.isEmpty());

        // Now, we need to check if our tables has correct columns
        cursor = sqLiteDatabase.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for the table information.", cursor.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieEntryColumnHashSet = new HashSet<>();

        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RUNTIME);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_GENRES);
        movieEntryColumnHashSet.add(MovieContract.MovieEntry.COLUMN_FAVORITES);

        int columnNameIndex = cursor.getColumnIndex("name");

        do {
            String columnName = cursor.getString(columnNameIndex);
            movieEntryColumnHashSet.remove(columnName);
        }while(cursor.moveToNext());

        // If this fails, it means that your database doesn't contain all of the
        // required movie entry columns
        assertTrue("Error: The database doesn't contain all fo the required movie entry columns", movieEntryColumnHashSet.isEmpty());

        sqLiteDatabase.close();
    }

    // Using createMovieEntry() in TestUtilities.java
    public void testMovieEntryTable()
    {
        // Getting reference to the Writable Database
        insertMovieEntry();
    }

    public void testMovieReviews()
    {
        long movieEntryRowId = insertMovieEntry();

        // Make Sure that we have a valid Row ID
        assertFalse("Error: Movie Entry not Inserted Correctly", movieEntryRowId == -1L );

        // First step: Get reference to writable Database
        // If there's an error in those massive SQL Table Creation Strings,
        // errors will be thrown here when you try to get a writable database
        MovieDBHelper movieDBHelper = new MovieDBHelper(mContext);
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();

        Cursor col_cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME, null);

        int row_count = col_cursor.getCount();

        assertFalse("Error: No Entries found in MovieEntry Table", row_count <= 0);

        int movie_id = 0;

        if(col_cursor.moveToFirst())
        {
           movie_id =  col_cursor.getInt(col_cursor.getColumnIndex("movie_id"));
        }

        // Second step (Movie Review): Create Movie Review Values
        ContentValues movieReviewValues = TestUtilities.createMovieReviews();

        long movieReviewId = sqLiteDatabase.insert(MovieContract.MovieReviews.TABLE_NAME, null, movieReviewValues);

        assertFalse("Error: Movie Reviews not inserted correctly", movieReviewId == -1);

        Cursor cursor = sqLiteDatabase.query(MovieContract.MovieReviews.TABLE_NAME, null, null, null, null, null, null);

        // Making sure that we got a row backs
        assertTrue("Error: No records returned from MovieReview query", cursor.moveToFirst());

        // Trying to check if the Movie Reviews conform with the Database Requirements
        if(cursor.moveToFirst())
        {
            assertTrue("Error: Bad Records in the Movie Review Table", cursor.getInt(cursor.getColumnIndex("movie_id")) == movie_id);
        }

        // Validating the Records inserted
        TestUtilities.validateCurrentRecord("Error: Movie Review Query Validation Failed", cursor, movieReviewValues);

        // Closing the Cursors and Database
        col_cursor.close();
        cursor.close();
        movieDBHelper.close();
    }

    public long insertMovieEntry()
    {
        // Getting a Reference for Writable Database
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Second Step Create ContentValue of the what you want to insert
        ContentValues testValues = TestUtilities.createMovieEntry();

        // Third Step: Insert ContentValues into database and get a row ID back
        long movieRowId;
        movieRowId = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // IN THEORY, Data's Inserted. Now pull some out to stare at it and verify it made
        // The round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query.
        assertTrue("Error: No Records returned from MovieEntry query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the Original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Movie Entry Query Validation Failed", cursor, testValues);

        // Checking if there is more than one Record
        assertFalse("Error: More than one record returned from MovieEntry Query", cursor.moveToNext());

        // Sixth Step: Close Cursor and Database
        cursor.close();
        dbHelper.close();
        return(movieRowId);
    }

    /* This tears down the test */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}