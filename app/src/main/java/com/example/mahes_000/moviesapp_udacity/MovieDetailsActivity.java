package com.example.mahes_000.moviesapp_udacity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MovieDetailsActivity extends AppCompatActivity {

    ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);

        // View Pager is adapter to translate between different pages
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Setting the Adapter for ViewPager
        viewPager.setAdapter(new MyAdapter(fragmentManager));

        // Setting up Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
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
}

class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

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

    // WhenEver a ViewPager wants to display a page it will call getCount() Method that tells exactly how many pages are there.
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

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
    }
}
