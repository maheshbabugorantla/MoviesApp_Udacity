package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

/*
    private Toolbar toolbar;
    public CollapsingToolbarLayout collapsingToolbarLayout = null;
*/
    ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsible_layout);

/*
        // View Pager is adapter to translate between different pages
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Setting the Adapter for ViewPager
        viewPager.setAdapter(new MyAdapter(fragmentManager));

        // Setting up Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
*/
        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String movie_details = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] str_values = movie_details.split("=");

            String Image_Base_URL = "http://image.tmdb.org/t/p/w500/";
            String Image_URL = Image_Base_URL + str_values[8];

            // Setting the Image for the Poster
            Picasso.with(getApplicationContext()).load(Image_URL).fit().into((ImageView) findViewById(R.id.movie_poster));

            setupToolbar();
            setupViewPager();
            setupCollapsingToolbar(str_values[2]);
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

/*    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.expandedappbar);
    }*/
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        getSupportActionBar().setTitle(Title);
*/
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

/*
        Fragment fragment = null;

        if(position == 0)
        {
            fragment = new MovieDetailsActivityFragment();
        }
        else if(position == 1)
        {
            fragment = new Reviews_Fragment();
        }
        else if(position == 2)
        {
            fragment = new Video_Fragment();
        }

        return fragment;
    }
*/

    // WhenEver a ViewPager wants to display a page it will call getCount() Method that tells exactly how many pages are there.
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }

/*
        if(position == 0)
        {
            return "DETAILS";
        }

        else if(position == 1)
        {
            return "REVIEWS";
        }

        else if(position == 2)
        {
            return "TRAILERS";
        }

        return null;
    }*/
}
