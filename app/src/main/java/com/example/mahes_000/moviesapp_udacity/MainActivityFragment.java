package com.example.mahes_000.moviesapp_udacity;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahes_000.moviesapp_udacity.Adapters.MovieCursorAdapter;
import com.example.mahes_000.moviesapp_udacity.FetchDataTasks.FetchMovieData;
import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;


import java.util.concurrent.ExecutionException;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> { //, FetchMovieDataInterface

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private GridView gridView;
    private ProgressBar progressBar;
//    private GridViewAdapter gridViewAdapter;
    //private ArrayList<ImageItem> mGridData;

    private MovieCursorAdapter mMovieCursorAdapter;

    // Identifier for CURSOR Loader
    private static final int MOVIES_LOADER = 0;

/*
    String Movies_Data = null; // This will contain the raw JSON Data that needs to parsed.

    int scrollIndex = 0;
*/

    private Parcelable gridState = null;
    private static final String LIST_STATE = "liststate";

/*
    ArrayList<String> movie_desc = new ArrayList<>();
*/

    // This is used to select between the "popular" and "top_rated"
    String Movies_Choice = "top_rated";
    String Video_Choice = "movie";

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        gridView.setAdapter(mMovieCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetailActivity = new Intent(getActivity(), MovieDetailsActivity.class).putExtra(Intent.EXTRA_TEXT, ((TextView)view.findViewById(R.id.ID_val)).getText()); //movie_desc.get(position));

                startActivity(toDetailActivity);
            }
        });

        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                if (page == 1) {
                    return false;
                } else {
                    return LoadMoreData(Integer.toString(page));
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        return (rootView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

        if (savedInstanceState != null) {
            gridState = savedInstanceState.getParcelable(LIST_STATE);
            Log.d("In onActivityCreated", "Retrieving the Saved Instance State");
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.d("In OnSaveInstanceState", "Saving the state of the app");
        gridState = gridView.onSaveInstanceState();
        state.putParcelable(LIST_STATE, gridState);
    }

    // This function is called when the fragment is available for the user to Start Interacting
    @Override
    public void onResume() {


        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);

        if (gridState != null) {
            gridView.onRestoreInstanceState(gridState);
            Log.d("MainActivityFragment", "trying to restore gridView state..");
        }

        gridState = null;
        super.onResume();

    }

    @Override
    public void onPause() {

//        scrollIndex = gridView.getFirstVisiblePosition();
        gridState = gridView.onSaveInstanceState();
        Log.d("MainActivityFragment", "trying to save gridView state..");
        super.onPause();
    }


    public boolean LoadMoreData(String page) {

        if (Utility.isNetworkAvailable(getActivity())) {
            FetchMovieData movieData = new FetchMovieData(getActivity()); /*MainActivityFragment.this);*/

            Log.i(LOG_TAG, "Inside LoadMoreData ");

            try {

                movieData.execute(Movies_Choice, Video_Choice, page).get();

                mMovieCursorAdapter.notifyDataSetChanged();

                return true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Unable to get more movies, No Internet available.\nConnect to the internet and Re-open the App", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    // This pull all the data from theMovieDB using the default settings as soon as the app starts
    @Override
    public void onStart() {
        super.onStart();

        Log.d("Inside OnStart Function", "onStart Started");

/*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
*/
        Movies_Choice = Utility.getRatingChoice(getActivity()); //sharedPreferences.getString(getString(R.string.pref_rating_choice_key), getString(R.string.pref_rating_choice_default));
        Video_Choice = Utility.getVideoChoice(getActivity()); //sharedPreferences.getString(getString(R.string.pref_video_choice_key), getString(R.string.pref_video_choice_default));

        System.out.println("Movies Choice " + Movies_Choice);
        System.out.println("Videos Choice " + Video_Choice);

        if (Utility.isNetworkAvailable(getActivity())) {

            Log.d("MainActivityFragment ", "Inside OnStart() Function");
            FetchMovieData movieData = new FetchMovieData(getActivity());/*, MainActivityFragment.this);*/

            try {

                movieData.execute(Movies_Choice, Video_Choice, "1").get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            mMovieCursorAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Make Sure that you are connected to Internet and Re-open the App", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Movies_Choice =  Utility.getRatingChoice(getActivity());
        Video_Choice = Utility.getVideoChoice(getActivity());

        // The code below is used to query the database
        String sortOrder;

        if (Movies_Choice.equals("top_rated")) {
            sortOrder = MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
        } else {
            sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
        }

        if (Video_Choice.equals("movie")) {
            return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, sortOrder);
        } else if (Video_Choice.equals("tv")) {
            return new CursorLoader(getActivity(), MovieContract.TVEntry.CONTENT_URI, null, null, null, sortOrder);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieCursorAdapter.notifyDataSetChanged();
        mMovieCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.i("Inside onLoaderReset", "Swapped the Cursor");

        mMovieCursorAdapter.notifyDataSetChanged();
        mMovieCursorAdapter.swapCursor(null);
    }
}