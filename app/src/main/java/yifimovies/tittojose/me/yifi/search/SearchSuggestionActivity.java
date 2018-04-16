package yifimovies.tittojose.me.yifi.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yifimovies.tittojose.me.yifi.homescreen.MoviesRecyclerAdapter;
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient;
import yifimovies.tittojose.me.yifi.api.MoviesService;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;

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


    private MoviesService moviesService;
    private Call<MovieAPIResponse> suggestionAPICall;
    private List<Object> movies = new ArrayList<>();
    private MoviesRecyclerAdapter mAdapter;
    private Callback<MovieAPIResponse> apiCallback = new Callback<MovieAPIResponse>() {
        @Override
        public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
            if (response.isSuccessful()) {
                progressLayout.setVisibility(View.GONE);
                if (response.body() != null && response.body().getData() != null && response.body().getData().getMovies() != null && response.body().getData().getMovies().size() > 0) {
//                    movies = response.body().getData().getMovies();
                    try {
                        movies.addAll(response.body().getData().getMovies());
                        mAdapter = new MoviesRecyclerAdapter(SearchSuggestionActivity.this, movies, recyclerAdapterListener);
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
                Snackbar snackbar = Snackbar
                        .make(parentCoordinatorLayout, "Network error. Please try again.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        @Override
        public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
            //TODO : Handle error.
            progressLayout.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(parentCoordinatorLayout, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    };

    private MoviesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = new MoviesRecyclerAdapter.MoviesRecyclerAdapterListener() {
        @Override
        public void onItemClickListener(Movie movie, ImageView imageView) {
            Intent i = new Intent(SearchSuggestionActivity.this, MovieDetailActivity.class);
            i.putExtra("movie", movie);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
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
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!searchEditText.getText().toString().isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, searchEditText.getText().toString());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        String queryString = searchEditText.getText().toString();
                        moviesService = MoviesAPIClient.getMoviesAPIService();
                        suggestionAPICall = moviesService.getMovieSuggestions(queryString);
                        suggestionAPICall.enqueue(apiCallback);
                        mAdapter = new MoviesRecyclerAdapter(SearchSuggestionActivity.this, movies, recyclerAdapterListener);
                        movieSuggestionsRecyclerView.setAdapter(mAdapter);
                        progressLayout.setVisibility(View.VISIBLE);
                        return true;
                    }else {
                       Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Empty search");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    }
                }
                return false;
            }
        });


        final GridLayoutManager layoutManager = new GridLayoutManager(SearchSuggestionActivity.this, 2);
        movieSuggestionsRecyclerView.setLayoutManager(layoutManager);
        movieSuggestionsRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
