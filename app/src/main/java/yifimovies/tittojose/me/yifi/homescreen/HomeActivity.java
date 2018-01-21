package yifimovies.tittojose.me.yifi.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.search.SearchSuggestionActivity;
import yifimovies.tittojose.me.yifi.utils.NetworkUtils;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewMoviesSuggestionsList)
    RecyclerView movieSuggestionRecyclerView;


    @BindView(R.id.coordinatorLayoutHome)
    CoordinatorLayout parentCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        setupSuggestionsRecyclerView();

        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Snackbar snackbar = Snackbar
                    .make(parentCoordinatorLayout, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
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

    public void handleError(ResponseBody responseBody) {
        Snackbar snackbar = Snackbar
                .make(parentCoordinatorLayout, "Network error. Please try again.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
