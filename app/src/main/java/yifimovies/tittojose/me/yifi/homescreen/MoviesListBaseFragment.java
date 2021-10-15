package yifimovies.tittojose.me.yifi.homescreen;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient;
import yifimovies.tittojose.me.yifi.api.MoviesService;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity;

/**
 * Created by titto.jose on 21-12-2017.
 */

public abstract class MoviesListBaseFragment extends Fragment {

    private static final String TAG = MoviesListBaseFragment.class.getSimpleName();
    public MoviesService moviesService;

    @BindView(R.id.recyclerViewMoviesList)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.progressPagination)
    ProgressBar paginationProgressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorLayout)
    ViewGroup errorLayout;

    private List<Object> movies = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private boolean isLoading = true;
    public final int PAGE_SIZE = 20;
    public int page = 0;
    int lastAdPosition = -1;
    private boolean isLastPage = false;
    int retryCount = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            page = 0;
        }
        initializeMoviesList();
    }

    private void initializeMoviesList() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 0;
            loadMovieData(page++);
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == MoviesRecyclerAdapter.MOVIE) {
                    return 1;
                }
                return -1;
            }
        });
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


    private MoviesRecyclerAdapter.MoviesRecyclerAdapterListener recyclerAdapterListener = new MoviesRecyclerAdapter.MoviesRecyclerAdapterListener() {
        @Override
        public void onItemClickListener(Movie movie, ImageView imageView) {
            ActivityOptions options = null;
            Intent i = new Intent(getActivity(), MovieDetailActivity.class);
            i.putExtra("movie", movie);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    };

    public Callback<MovieAPIResponse> apiCallback = new Callback<MovieAPIResponse>() {
        @Override
        public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
            try {
                if (response.isSuccessful()) {
                    errorLayout.setVisibility(View.GONE);
                    moviesRecyclerView.setVisibility(View.VISIBLE);

                    paginationProgressBar.setVisibility(View.GONE);
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.body().getData().getMovies() != null && response.body().getData().getMovies().size() > 0) {

                        if (page == 1) {
                            movies = new ArrayList<>();
                            movies.addAll(response.body().getData().getMovies());
                            mAdapter = new MoviesRecyclerAdapter(getActivity(), movies, recyclerAdapterListener);
                            moviesRecyclerView.setAdapter(mAdapter);

                            Log.d(TAG, "onResponse: ");
                            lastAdPosition = -1;
                        } else {
                            movies.addAll(response.body().getData().getMovies());
                            mAdapter.notifyDataSetChanged();
                        }

                        int currentTotalItem = page * PAGE_SIZE;

                        if (currentTotalItem >= response.body().getData().getMovieCount()) {
                            isLastPage = true;
                        }
                    }
                } else {

                    Log.d(TAG, "onResponse: " + response.errorBody().toString());

                    if (retryCount == 0) {
                        retryCount++;
                        retryAPI();
                    } else {

                        try {
                            if (movies.size() == 0) {
                                moviesRecyclerView.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.VISIBLE);
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            ((HomeActivity) getActivity()).handleError("");
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.toString());
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "onResponse: " + e.toString());
            }

        }

        @Override
        public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
            if (retryCount == 0) {
                retryCount++;
                retryAPI();
            } else {

                try {
                    if (movies.size() == 0) {
                        moviesRecyclerView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                    paginationProgressBar.setVisibility(View.GONE);
                    ((HomeActivity) getActivity()).handleError("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public MoviesListBaseFragment() {
        // Required empty public constructor
    }

    private void loadMovieData(final int page) {
        isLoading = true;
        if (page >= 1) {
            paginationProgressBar.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
        moviesService = MoviesAPIClient.getMoviesAPIService();
        makeMoviesAPICall();
    }

    private void retryAPI() {
        Log.d("OkHttp", "retryAPI: ");
        moviesService = MoviesAPIClient.getMoviesAPIFallbackService();
        makeMoviesAPICall();
    }

    protected abstract void makeMoviesAPICall();
}
