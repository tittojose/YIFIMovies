package yifimovies.tittojose.me.yifi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    MoviesService moviesService;

    @BindView(R.id.recyclerViewMoviesList)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.progressPagination)
    ProgressBar paginationProgressBar;

    private List<Movie> movies;
    private RecyclerView.Adapter mAdapter;
    private boolean isLoading = true;
    private final int PAGE_SIZE = 10;
    private int page = 10;
    private boolean isLastPage = false;
    private MovesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = new MovesRecyclerAdapter.MoviesRecyclerAdapterListener() {
        @Override
        public void onItemClickListener(Movie movie, ImageView imageView) {
            ActivityOptions options = null;
            Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
            i.putExtra("movie", movie);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//                options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, getString(R.string.picture_transition_name));
//
//                ActivityCompat.startActivity(MainActivity.this, i, options.toBundle());
//            } else {

            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        loadMovieData(page++);
                    }
                }
            }
        });


        loadMovieData(page++);
    }

    private void loadMovieData(final int page) {
        isLoading = true;
        if (page > 1) {
            paginationProgressBar.setVisibility(View.VISIBLE);
        }
        moviesService = MoviesAPIClient.getMoviesAPIService();

        moviesService.getLatestMovies(page, PAGE_SIZE).enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                paginationProgressBar.setVisibility(View.GONE);
                isLoading = false;
                if (mAdapter == null) {
                    movies = response.body().getData().getMovies();
                    mAdapter = new MovesRecyclerAdapter(MainActivity.this, movies, recyclerAdapterListener);
                    moviesRecyclerView.setAdapter(mAdapter);
                } else {
                    movies.addAll(response.body().getData().getMovies());
                    mAdapter.notifyDataSetChanged();
                }

                int currentTotalItem = page * PAGE_SIZE;

                if (currentTotalItem >= response.body().getData().getMovieCount()) {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
                isLoading = false;
                paginationProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
