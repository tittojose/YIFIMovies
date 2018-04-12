package yifimovies.tittojose.me.yifi.bookmark;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.bookmark.model.BookmarkPrefModel;
import yifimovies.tittojose.me.yifi.homescreen.MoviesRecyclerAdapter;
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity;

public class BookmarkActivity extends AppCompatActivity {

    private static final String AD_PLACEMENT_ID = "334553013694096_370926686723395";
    public static final String TAG = "BookmarkActivity";

    @BindView(R.id.recyclerViewBookmarkedMoviesList)
    RecyclerView bookmarkedRecyclerView;

    @BindView(R.id.toolbarBookmarkedMovies)
    Toolbar toolbar;

    @BindView(R.id.tveEmptyBookmark)
    TextView emptyBookmark;

    private MoviesRecyclerAdapter mAdapter;
    private int lastAdPosition = -1;
    private int ADS_PER_ITEMS = 7;
    private List<Object> listBookmarkedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listBookmarkedMovies = new ArrayList<>();
        listBookmarkedMovies.addAll(BookmarkPrefModel.getListBookmarkedMovies(BookmarkActivity.this));

        if (listBookmarkedMovies.size() > 0) {

            bookmarkedRecyclerView.setVisibility(View.VISIBLE);
            emptyBookmark.setVisibility(View.GONE);

            mAdapter = new MoviesRecyclerAdapter(BookmarkActivity.this, listBookmarkedMovies, new MoviesRecyclerAdapter.MoviesRecyclerAdapterListener() {
                @Override
                public void onItemClickListener(Movie movie, ImageView imageView) {
                    ActivityOptions options = null;
                    Intent i = new Intent(BookmarkActivity.this, MovieDetailActivity.class);
                    i.putExtra("movie", movie);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            });

            final GridLayoutManager layoutManager = new GridLayoutManager(BookmarkActivity.this, 2);
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


            bookmarkedRecyclerView.setLayoutManager(layoutManager);
//        bookmarkedRecyclerView.setHasFixedSize(true);

            bookmarkedRecyclerView.setAdapter(mAdapter);
            loadAdsToList(listBookmarkedMovies.size());
        } else {
            bookmarkedRecyclerView.setVisibility(View.GONE);
            emptyBookmark.setVisibility(View.VISIBLE);
        }
    }


    private void loadAdsToList(final int size) {

        final int numberOfAds = size / 6;

        try {
            final NativeAdsManager mAds = new NativeAdsManager(BookmarkActivity.this, AD_PLACEMENT_ID, numberOfAds);

            mAds.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    try {
                        int num = numberOfAds;
                        while (num > 0 && lastAdPosition + ADS_PER_ITEMS < size) {
                            NativeAd nativeAd1 = mAds.nextNativeAd();
                            lastAdPosition += ADS_PER_ITEMS;
                            num--;
                            listBookmarkedMovies.add(lastAdPosition, nativeAd1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
