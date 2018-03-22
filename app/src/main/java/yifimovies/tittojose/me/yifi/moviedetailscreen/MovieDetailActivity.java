package yifimovies.tittojose.me.yifi.moviedetailscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yifimovies.tittojose.me.yifi.Constants;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.Torrent;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static String YOUTUBE_API_KEY = "AIzaSyBcdx13v4s97QAxxM921ka4WGfCt_91CCU";
    private static String FB_AD_BANNER_ID = "334553013694096_358571807958883";


    @BindView(R.id.imageViewMovieTitleImage)
    ImageView movieTitleImage;

    @BindView(R.id.tvMovieTitle)
    TextView movieTitleText;

    @BindView(R.id.tvMovieDescription)
    TextView movieDescriptionText;

    @BindView(R.id.btn720Download)
    Button hd720pDownload;

    @BindView(R.id.btn10800Download)
    Button fullHD1080pDownload;

    @BindView(R.id.btn3DDownload)
    Button threeDDownload;

    @BindView(R.id.tvMovieYear)
    TextView movieYearTextView;

    @BindView(R.id.tvMoviePlayTime)
    TextView moviePlayTimeTextView;

    @BindView(R.id.tvMovieRating)
    TextView movieRatingTextView;

    @BindView(R.id.rvMovieGenre)
    RecyclerView movieGenreRecyclerView;

    @BindView(R.id.rvMovieDownloads)
    RecyclerView movieDownloadRecyclerView;

    @BindView(R.id.layoutTorrentDownloads)
    ViewGroup downloadsLayout;

    @BindView(R.id.youtubeViewMovieTrailerContainer)
    ViewGroup youtubeTrailerContainer;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String[] torrentDownloadStrings = new String[3];

    private FirebaseAnalytics mFirebaseAnalytics;
    private Movie movie;
    private String downloadLinksURL = "";
    private View adContainer;
    private String AD_PLACEMENT_ID = "334553013694096_358571807958883";
    private ImageView adImage;
    private TextView tvAdTitle;
    private TextView tvAdBody;
    private Button btnCTA;
    private LinearLayout adChoicesContainer;
    private MediaView mediaView;
    private NativeAd mAds;
    private LinearLayout nativeAdContainer;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;

    @OnClick({R.id.btn3DDownload, R.id.btn10800Download, R.id.btn720Download})
    public void onDownloadClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.btn720Download:
                url = torrentDownloadStrings[0];
                break;
            case R.id.btn10800Download:
                url = torrentDownloadStrings[1];
                break;
            case R.id.btn3DDownload:
                url = torrentDownloadStrings[2];
                break;
        }

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse(url), "application/x-bittorrent");
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        } catch (Exception e) {
            try {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.trorrent_app_redirect_message, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);

                snackbar.setAction("Search", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TORRENT_APP_LINK)));
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TORRENT_APP_LINK)));
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
                snackbar.show();

            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movie = (Movie) getIntent().getSerializableExtra("movie");

        try {
            Glide.with(MovieDetailActivity.this)
                    .load(movie.getLargeCoverImage())
                    .apply(new RequestOptions().centerCrop())
                    .into(movieTitleImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        movieTitleText.setText(movie.getTitle());
        movieDescriptionText.setText(movie.getDescriptionFull());
        movieYearTextView.setText(String.format("%d", (long) movie.getYear()));
        moviePlayTimeTextView.setText(movie.getRuntime() + " mins");
        movieRatingTextView.setText(movie.getRating() + " stars");
        initializeMovieGenreList(movie.getGenres());
        initializeDownloadList(movie.getTorrents());
        initializeDownloadButtons(movie);
        initializeYoutubeTrailerView();
//        initializeBannerAdd();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MovieDetail");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MovieDetail");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        loadNativeAds();
    }


    private void loadNativeAds() {

        this.adContainer = findViewById(R.id.nativeAdContainer);
        adImage = findViewById(R.id.adImage);
        tvAdTitle = findViewById(R.id.tvAdTitle);
        tvAdBody = findViewById(R.id.tvAdBody);
        btnCTA = findViewById(R.id.btnCTA);
        adChoicesContainer = findViewById(R.id.adChoicesContainer);
        mediaView = findViewById(R.id.mediaView);

        try {
            mAds = new NativeAd(MovieDetailActivity.this, AD_PLACEMENT_ID);
            mAds.setAdListener(new AdListener() {

                @Override
                public void onError(Ad ad, AdError error) {
                    // Ad error callback
                    nativeAdContainer = findViewById(R.id.native_ad_container);
                    nativeAdContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Ad loaded callback
                    try {
                        if (ad != null) {
                            mAds.unregisterView();
                        }


                        nativeAdContainer = findViewById(R.id.native_ad_container);
                        nativeAdContainer.setVisibility(View.VISIBLE);

                        tvAdTitle.setText(mAds.getAdTitle());
                        tvAdBody.setText(mAds.getAdBody());
                        NativeAd.downloadAndDisplayImage(mAds.getAdIcon(), adImage);
                        btnCTA.setText(mAds.getAdCallToAction());
                        AdChoicesView adChoicesView = new AdChoicesView(MovieDetailActivity.this, mAds, true);
                        adChoicesContainer.removeAllViews();
                        adChoicesContainer.addView(adChoicesView);
                        mediaView.setNativeAd(mAds);

                        List<View> clickableViews = new ArrayList<>();
                        clickableViews.add(adImage);
                        clickableViews.add(btnCTA);
                        clickableViews.add(mediaView);
                        mAds.registerViewForInteraction(nativeAdContainer, clickableViews);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            });

            mAds.loadAd();
//            mAds.loadAds();
        } catch (Exception e) {
            Log.e(TAG, "loadAdsToList: " + e.toString());
        }
    }

//    private void initializeBannerAdd() {
////        adView = new AdView(this, FB_AD_BANNER_ID, AdSize.BANNER_HEIGHT_90);
//
////        adBannerContainer.addView(adView);
//
//        // Request an ad
////        adView.loadAd();
//    }

    private void initializeYoutubeTrailerView() {

        if (movie.getYtTrailerCode() != null && !movie.getYtTrailerCode().isEmpty()) {
            YouTubePlayerSupportFragment mYoutubePlayerFragment = new YouTubePlayerSupportFragment();
            mYoutubePlayerFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        youTubePlayer.cueVideo(movie.getYtTrailerCode());
                        playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {

                            }

                            @Override
                            public void onLoaded(String s) {

                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "YoutubePlayerStarted");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "YoutubePlayerStarted");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "YoutubePlayer");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                            }

                            @Override
                            public void onVideoEnded() {

                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                            }
                        };
                        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean b) {
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "YoutubePlayerFullscreen");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "YoutubePlayerFullscreen");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "YoutubePlayer");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                            }
                        });
                        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                    } else {
                        hideYTTrailerLayout();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Toast.makeText(MovieDetailActivity.this, "Youtube initialized error", Toast.LENGTH_SHORT);

                }
            });
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.youtubeViewMovieTrailer, mYoutubePlayerFragment);
            fragmentTransaction.commit();
        } else {
            hideYTTrailerLayout();
        }
    }

    private void hideYTTrailerLayout() {
        youtubeTrailerContainer.setVisibility(View.GONE);
    }

    private void initializeMovieGenreList(List<String> genres) {
        if (genres != null && genres.size() >= 0) {
            MovieGenreRecyclerAdapter movieGenreRecyclerAdapter = new MovieGenreRecyclerAdapter(MovieDetailActivity.this, genres);
            final GridLayoutManager layoutManager = new GridLayoutManager(MovieDetailActivity.this, 3);
            movieGenreRecyclerView.setLayoutManager(layoutManager);
            movieGenreRecyclerView.setHasFixedSize(true);
            movieGenreRecyclerView.setAdapter(movieGenreRecyclerAdapter);
        } else {

        }

    }


    private void initializeDownloadList(List<Torrent> torrents) {
        if (torrents != null && torrents.size() >= 0) {
            MovieTorrentDownloaderAdapter movieGenreRecyclerAdapter = new MovieTorrentDownloaderAdapter(MovieDetailActivity.this, movie.getTitle(), torrents, new MovieTorrentDownloaderAdapter.DownloadTorrentClickListener() {
                @Override
                public void onDownloadTorrentClicked(String torrentLink) {

                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.parse(torrentLink), "application/x-bittorrent");
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                    } catch (Exception e) {
                        launchTorrentAppSearch();
                    }
                }

                @Override
                public void onDownloadMagnetClicked(String torrentLink) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(torrentLink)));
                    } catch (Exception e) {
                        launchTorrentAppSearch();
                    }
                }
            });
            movieDownloadRecyclerView.setHasFixedSize(true);
            movieDownloadRecyclerView.setAdapter(movieGenreRecyclerAdapter);
        } else {

        }
    }

    private void launchTorrentAppSearch() {
        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.trorrent_app_redirect_message, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(5);

            snackbar.setAction("Search", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TORRENT_APP_LINK)));
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TORRENT_APP_LINK)));
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onShown(Snackbar snackbar) {

                }
            });
            snackbar.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDownloadButtons(Movie movie) {
        if (movie.getTorrents() != null && movie.getTorrents().size() > 0) {
            for (Torrent torrent : movie.getTorrents()) {
                if (torrent.getQuality().equalsIgnoreCase("720p")) {
//                    hd720pDownload.setVisibility(View.VISIBLE);
                    torrentDownloadStrings[0] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("720p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("1080p")) {
//                    fullHD1080pDownload.setVisibility(View.VISIBLE);
                    torrentDownloadStrings[1] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("1080p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("3D")) {
//                    threeDDownload.setVisibility(View.VISIBLE);
                    torrentDownloadStrings[2] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("3D", torrent.getUrl());
                }
            }
        } else {
            downloadsLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_screen_menu, menu);

        MenuItem item = menu.findItem(R.id.share_movie_torrent);
        if (movie.getTorrents() != null && movie.getTorrents().size() > 0) {
            item.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_movie_torrent:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "YiFy Torrents App");
                intent.putExtra(Intent.EXTRA_TEXT, generateShareEmailBody());
                startActivity(Intent.createChooser(intent, "Share movie torrent"));

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ShareMovieTorrent");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ShareMovieTorrent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                break;

            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private String generateShareEmailBody() {
        String movieTitle = movie.getTitle() + " - " + movie.getYear();
        String downloadLinks = this.downloadLinksURL;


        return movieTitle + "\n\n" + downloadLinks + "\n\n" + getString(R.string.share_torrent_app_promo_text);
    }


    private void generateShareEmailBodyDownloadLinks(String type, String url) {
        String downloadLink = "\t" + type + " - " + url + "\n\n";
        this.downloadLinksURL += downloadLink;
    }

    @Override
    protected void onDestroy() {
        if (mAds != null) {
            mAds.destroy();
        }
        super.onDestroy();

    }
}
