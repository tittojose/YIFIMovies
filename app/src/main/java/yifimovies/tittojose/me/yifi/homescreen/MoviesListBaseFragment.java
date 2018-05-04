package yifimovies.tittojose.me.yifi.homescreen;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

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

    public String AD_PLACEMENT_ID;

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
    int ADS_PER_ITEMS = 7;
    private boolean isLastPage = false;

    int retryCount = 0;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        setAdPlacementId();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loadMovieData(page++);
            }
        });


        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case MoviesRecyclerAdapter.NATIVE_AD:
                        return 2;
                    case MoviesRecyclerAdapter.MOVIE:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        moviesRecyclerView.setLayoutManager(layoutManager);
//        moviesRecyclerView.setHasFixedSize(true);
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
                            loadAdsToList();
                        } else {
                            movies.addAll(response.body().getData().getMovies());
                            mAdapter.notifyDataSetChanged();
                            loadAdsToList();
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


    private void loadAdsToList() {
        try {
            final NativeAdsManager mAds = new NativeAdsManager(getActivity(), AD_PLACEMENT_ID, 3);

            mAds.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    try {
                        NativeAd nativeAd1 = mAds.nextNativeAd();
                        NativeAd nativeAd2 = mAds.nextNativeAd();
                        NativeAd nativeAd3 = mAds.nextNativeAd();

                        if (lastAdPosition + ADS_PER_ITEMS < movies.size()) {
                            lastAdPosition += ADS_PER_ITEMS;
                            movies.add(lastAdPosition, nativeAd1);
                        }

                        if (lastAdPosition + ADS_PER_ITEMS < movies.size()) {
                            lastAdPosition += ADS_PER_ITEMS;
                            movies.add(lastAdPosition, nativeAd2);
                        }

                        if (lastAdPosition + ADS_PER_ITEMS < movies.size()) {
                            lastAdPosition += ADS_PER_ITEMS;
                            movies.add(lastAdPosition, nativeAd3);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onAdError(AdError adError) {

                }
            });

            mAds.loadAds();
        } catch (Exception e) {
            Log.e(TAG, "loadAdsToList: " + e.toString());
        }
    }


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

    protected abstract void setAdPlacementId();

}
