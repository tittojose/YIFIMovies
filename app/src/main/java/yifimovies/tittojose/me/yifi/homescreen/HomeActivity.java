package yifimovies.tittojose.me.yifi.homescreen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupNavigationDrawer();
        setupViewPager(viewPager);
        setupSuggestionsRecyclerView();

        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Snackbar snackbar = Snackbar
                    .make(parentCoordinatorLayout, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    private void setupNavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_rate_app:
                        startRateAppIntent();
                        break;
                    case R.id.drawer_share_app:
                        startShareAppIntent();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void startShareAppIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.shared_via));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void startRateAppIntent() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
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

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
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

    public void handleError(String responseBody) {
        Snackbar snackbar = Snackbar
                .make(parentCoordinatorLayout, "Network error. Please try again.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
