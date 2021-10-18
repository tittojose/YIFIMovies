package yifimovies.tittojose.me.yifi.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient;
import yifimovies.tittojose.me.yifi.api.MoviesService;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;
import yifimovies.tittojose.me.yifi.homescreen.MoviesRecyclerAdapter;
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity;

/**
 * Created by titto.jose on 16-01-2018.
 */

public class SearchSuggestionActivity extends AppCompatActivity {

    private static final String TAG = SearchSuggestionActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewMoviesSuggestionsList)
    RecyclerView movieSuggestionsRecyclerView;

    @BindView(R.id.progressLayout)
    ViewGroup progressLayout;

    @BindView(R.id.etSearch)
    EditText searchEditText;

    @BindView(R.id.coordinatorSearch)
    CoordinatorLayout parentCoordinatorLayout;

    int retryCount = 0;
    private MoviesService moviesService;
    private Call<MovieAPIResponse> suggestionAPICall;
    private List<Movie> movies = new ArrayList<>();
    private MoviesRecyclerAdapter mAdapter;
    private Callback<MovieAPIResponse> apiCallback = new Callback<MovieAPIResponse>() {
        @Override
        public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
            if (response.isSuccessful()) {
                progressLayout.setVisibility(View.GONE);
                if (response.body() != null && response.body().getData() != null && response.body().getData().getMovies() != null && response.body().getData().getMovies().size() > 0) {
                    try {
                        movies.addAll(response.body().getData().getMovies());
                        mAdapter = new MoviesRecyclerAdapter(movies, recyclerAdapterListener);
                        movieSuggestionsRecyclerView.setAdapter(mAdapter);
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.toString());
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(parentCoordinatorLayout, "No movies found. Please try a different keyword.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } else {
                if (retryCount == 0) {
                    String queryString = searchEditText.getText().toString();
                    retryApiCall(queryString);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(parentCoordinatorLayout, "Network error. Please try again.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }

        @Override
        public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
            if (retryCount == 0) {
                String queryString = searchEditText.getText().toString();
                retryApiCall(queryString);
            } else {
                progressLayout.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(parentCoordinatorLayout, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };


    private MoviesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = (movie) -> {
        Intent i = new Intent(SearchSuggestionActivity.this, MovieDetailActivity.class);
        i.putExtra("movie", movie);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    };
    private FirebaseAnalytics mFirebaseAnalytics;

    @OnClick(R.id.btnSearchBack)
    void onBackButtonClicked(View v) {
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_suggestions);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        searchEditText.requestFocus();
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!searchEditText.getText().toString().isEmpty()) {
                    movies = new ArrayList<Movie>();
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, searchEditText.getText().toString());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    String queryString = searchEditText.getText().toString();
                    makeApiCall(queryString);
                    mAdapter = new MoviesRecyclerAdapter(movies, recyclerAdapterListener);
                    movieSuggestionsRecyclerView.setAdapter(mAdapter);

                    return true;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Empty search");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                }
            }
            return false;
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(SearchSuggestionActivity.this, 2);
        movieSuggestionsRecyclerView.setLayoutManager(layoutManager);
        movieSuggestionsRecyclerView.setHasFixedSize(true);

    }

    private void makeApiCall(String queryString) {
        progressLayout.setVisibility(View.VISIBLE);
        moviesService = MoviesAPIClient.getMoviesAPIService();
        suggestionAPICall = moviesService.getMovieSuggestions(queryString);
        suggestionAPICall.enqueue(apiCallback);
    }

    private void retryApiCall(String queryString) {
        Log.d("OkHttp", "retryApiCall: ");
        retryCount++;
        progressLayout.setVisibility(View.VISIBLE);
        moviesService = MoviesAPIClient.getMoviesAPIFallbackService();
        suggestionAPICall = moviesService.getMovieSuggestions(queryString);
        suggestionAPICall.enqueue(apiCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
