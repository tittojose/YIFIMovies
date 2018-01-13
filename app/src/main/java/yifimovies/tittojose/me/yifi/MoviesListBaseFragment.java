package yifimovies.tittojose.me.yifi;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by titto.jose on 21-12-2017.
 */

public abstract class MoviesListBaseFragment extends Fragment {

    private static final String TAG = MoviesListBaseFragment.class.getSimpleName();
    MoviesService moviesService;

    @BindView(R.id.recyclerViewMoviesList)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.progressPagination)
    ProgressBar paginationProgressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Movie> movies;
    private RecyclerView.Adapter mAdapter;
    private boolean isLoading = true;
    final int PAGE_SIZE = 10;
    int page = 0;
    private boolean isLastPage = false;

    private MovesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = new MovesRecyclerAdapter.MoviesRecyclerAdapterListener() {
        @Override
        public void onItemClickListener(Movie movie, ImageView imageView) {
            ActivityOptions options = null;
            Intent i = new Intent(getActivity(), MovieDetailActivity.class);
            i.putExtra("movie", movie);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//                options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, getString(R.string.picture_transition_name));
//
//                ActivityCompat.startActivity(MainActivity.this, i, options.toBundle());
//            } else {

            startActivity(i);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//            }
        }
    };
    Callback<MovieAPIResponse> apiCallback = new Callback<MovieAPIResponse>() {
        @Override
        public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
            paginationProgressBar.setVisibility(View.GONE);
            isLoading = false;
            swipeRefreshLayout.setRefreshing(false);
            if (mAdapter == null) {
                movies = response.body().getData().getMovies();
                mAdapter = new MovesRecyclerAdapter(getActivity(), movies, recyclerAdapterListener);
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
            swipeRefreshLayout.setRefreshing(false);
            isLoading = false;
            paginationProgressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    };

    public MoviesListBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                mAdapter = null;
                loadMovieData(page++);
            }
        });


        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    private void loadMovieData(final int page) {
        isLoading = true;
        if (page >= 1) {
            paginationProgressBar.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        moviesService = MoviesAPIClient.getMoviesAPIService();
        makeMoviesAPICall();
    }

    protected abstract void makeMoviesAPICall();

}
