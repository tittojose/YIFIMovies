package yifimovies.tittojose.me.yifi.search;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yifimovies.tittojose.me.yifi.MovesRecyclerAdapter;
import yifimovies.tittojose.me.yifi.MovieDetailActivity;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient;
import yifimovies.tittojose.me.yifi.api.MoviesService;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;

/**
 * Created by titto.jose on 16-01-2018.
 */


public class SearchSuggestionActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewMoviesSuggestionsList)
    RecyclerView movieSuggestionsRecyclerView;

    @BindView(R.id.progressLayout)
    ViewGroup progressLayout;

    @BindView(R.id.etSearch)
    EditText searchEditText;
    private MoviesService moviesService;
    private Call<MovieAPIResponse> suggestionAPICall;
    private List<Movie> movies;
    private MovesRecyclerAdapter mAdapter;
    private Callback<MovieAPIResponse> apiCallback = new Callback<MovieAPIResponse>() {
        @Override
        public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
            progressLayout.setVisibility(View.GONE);
            movies = response.body().getData().getMovies();
            mAdapter = new MovesRecyclerAdapter(SearchSuggestionActivity.this, movies, recyclerAdapterListener);
            movieSuggestionsRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
            //TODO : Handle error.
            progressLayout.setVisibility(View.GONE);
        }
    };

    private MovesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = new MovesRecyclerAdapter.MoviesRecyclerAdapterListener() {
        @Override
        public void onItemClickListener(Movie movie, ImageView imageView) {
            Intent i = new Intent(SearchSuggestionActivity.this, MovieDetailActivity.class);
            i.putExtra("movie", movie);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    };

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


        searchEditText.requestFocus();
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String queryString = searchEditText.getText().toString();
                    moviesService = MoviesAPIClient.getMoviesAPIService();
                    suggestionAPICall = moviesService.getMovieSuggestions(queryString);
                    suggestionAPICall.enqueue(apiCallback);
                    Toast.makeText(SearchSuggestionActivity.this, queryString, Toast.LENGTH_SHORT).show();
                    mAdapter = new MovesRecyclerAdapter(SearchSuggestionActivity.this, new ArrayList<Movie>(), recyclerAdapterListener);
                    movieSuggestionsRecyclerView.setAdapter(mAdapter);
                    progressLayout.setVisibility(View.VISIBLE);
                    return true;
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
