package yifimovies.tittojose.me.yifi;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient;
import yifimovies.tittojose.me.yifi.api.MoviesService;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;
import yifimovies.tittojose.me.yifi.search.SearchSuggestionActivity;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewMoviesSuggestionsList)
    RecyclerView movieSuggestionRecyclerView;


    private Handler mHandler;
    private String mQueryString;
    private MoviesService moviesService;
    private Callback<MovieAPIResponse> apiCallback;
    private Call<MovieAPIResponse> suggestionAPICall;
    private List<Movie> suggestedMovies;
    private MovesSuggestionsRecyclerAdapter mAdapter;

    private MovieSearchCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        setupSuggestionsRecyclerView();

        mHandler = new Handler();
        apiCallback = new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                if (response.body().getData().getMovies() != null) {
//                    Toast.makeText(HomeActivity.this, "" + response.body().getData().getMovies().size(), Toast.LENGTH_SHORT).show();
                    //TODO: Handle data
//                    movieSuggestionRecyclerView.setVisibility(View.VISIBLE);
                    suggestedMovies = response.body().getData().getMovies();
//                    mAdapter = new MovesSuggestionsRecyclerAdapter(HomeActivity.this, suggestedMovies, null);
//                    movieSuggestionRecyclerView.setAdapter(mAdapter);

                    final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, "title", "image"});
                    for (int i = 0; i < suggestedMovies.size(); i++) {
                        mc.addRow(new Object[]{i, suggestedMovies.get(i).getTitle(), suggestedMovies.get(i).getSmallCoverImage()});
                    }
                    myAdapter.changeCursor(mc);

                } else {
//                    Toast.makeText(HomeActivity.this, "Empty search", Toast.LENGTH_SHORT).show();
                    //TODO: Handle empty search
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
//                Toast.makeText(HomeActivity.this, "suggestion error", Toast.LENGTH_SHORT).show();
                //TODO: Handle Error.
            }
        };

        myAdapter = new MovieSearchCursorAdapter(HomeActivity.this, null, null);

    }

    private void setupSuggestionsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        movieSuggestionRecyclerView.setLayoutManager(layoutManager);
        movieSuggestionRecyclerView.setHasFixedSize(true);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LatestMoviesFragment(), "Latest");
        adapter.addFragment(new TopMoviesFragment(), "Top");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO: Make API Call
//        Toast.makeText(HomeActivity.this, "Submitted - " + query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchTerm) {
//        Toast.makeText(HomeActivity.this, newText, Toast.LENGTH_SHORT).show();
        //use the query to search your data somehow
        mQueryString = searchTerm;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Put your call to the server here (with mQueryString)
                if (suggestionAPICall != null) {
                    suggestionAPICall.cancel();
                }
//                Toast.makeText(HomeActivity.this, mQueryString, Toast.LENGTH_SHORT).show();
                moviesService = MoviesAPIClient.getMoviesAPIService();
                suggestionAPICall = moviesService.getMovieSuggestions(mQueryString);
                suggestionAPICall.enqueue(apiCallback);

            }
        }, 600);
        return true;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

//         Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
//        searchView.setIconified(false);
//        searchView.setSuggestionsAdapter(myAdapter);

//
//        searchView.setOnQueryTextListener(this);
////        searchView.setSuggestionsAdapter(myAdapter);
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                myAdapter.changeCursor(null);
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(HomeActivity.this, SearchSuggestionActivity.class));
                overridePendingTransition(0, 0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (movieSuggestionRecyclerView.getVisibility() == View.VISIBLE) {
            movieSuggestionRecyclerView.setVisibility(View.GONE);
        }
    }
}
