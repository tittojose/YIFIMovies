package yifimovies.tittojose.me.yifi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
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

    MoviesService moviesService;

    @BindView(R.id.recyclerViewMoviesList)
    RecyclerView moviesRecyclerView;

    private List<Movie> movies;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);


        loadMovieData();
    }

    private void loadMovieData() {
        moviesService = MoviesAPIClient.getMoviesAPIService();

        moviesService.getLatestMovies().enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                movies = response.body().getData().getMovies();
                mAdapter = new MovesRecyclerAdapter(MainActivity.this, movies, new MovesRecyclerAdapter.MoviesRecyclerAdapterListener() {
                    @Override
                    public void onItemClickListener(Movie movie, ImageView imageView) {
                        ActivityOptions options = null;
                        Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
                        i.putExtra("movie", movie);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                            options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, getString(R.string.picture_transition_name));

                            ActivityCompat.startActivity(MainActivity.this, i, options.toBundle());
                        } else {

                            startActivity(i);
                        }
                    }
                });
                moviesRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
