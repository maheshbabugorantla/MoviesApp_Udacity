package com.example.mahes_000.moviesapp_udacity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    // Projections to fetch the required Data.
    String[] Movie_Columns = {MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_BACKDROP};

    String[] TV_Columns = {MovieContract.TVEntry.COLUMN_TITLE, MovieContract.TVEntry.COLUMN_BACKDROP};


    int COL_TITLE_INDEX = 0;
    int COL_BACKDROP_INDEX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsible_layout);

        Intent intent = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Video_Choice = sharedPreferences.getString(getString(R.string.pref_video_choice_key), getString(R.string.pref_video_choice_default));


        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String movie_details = intent.getStringExtra(Intent.EXTRA_TEXT);

            Cursor cursor = null;

            if(Video_Choice.equals("movie"))
            {
                cursor = getApplicationContext().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(movie_details)), Movie_Columns, null, null, null);
            }
            else if(Video_Choice.equals("tv"))
            {
                cursor = getApplicationContext().getContentResolver().query(MovieContract.TVEntry.buildTVUri(Long.parseLong(movie_details)), TV_Columns, null, null, null);
            }

            if(cursor.moveToFirst()) {

                String Image_Base_URL = "http://image.tmdb.org/t/p/w500/";
                String Image_URL = Image_Base_URL + cursor.getString(COL_BACKDROP_INDEX);

                // Setting the Image for the Poster
                Picasso.with(getApplicationContext()).load(Image_URL).fit().into((ImageView) findViewById(R.id.movie_poster));

                setupCollapsingToolbar(cursor.getString(COL_TITLE_INDEX));
            }

            setupToolbar();
            setupViewPager();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie__details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void markAsFavorite(View view) {
        Toast.makeText(this, "Movie added to the Favorites", Toast.LENGTH_LONG).show();
        return;
    }

    private void setupCollapsingToolbar(String title) {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addFrag(new MovieDetailsActivityFragment(), "DETAILS");
        adapter.addFrag(new Reviews_Fragment(), "REVIEWS");
        adapter.addFrag(new Video_Fragment(), "TRAILERS");

        viewPager.setAdapter(adapter);
    }

}

class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    // WhenEver a ViewPager wants to display a page it will call getCount() Method that tells exactly how many pages are there.
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }
}
