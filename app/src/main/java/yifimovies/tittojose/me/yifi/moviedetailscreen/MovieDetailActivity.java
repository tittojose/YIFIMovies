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
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.Torrent;

public class MovieDetailActivity extends AppCompatActivity {

    private static String YOUTUBE_API_KEY = "AIzaSyBcdx13v4s97QAxxM921ka4WGfCt_91CCU";
    private static String FB_AD_BANNER_ID = "334553013694096_343276972821700";


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
    RecyclerView movieGenereRecyclerView;

    @BindView(R.id.layoutTorrentDownloads)
    ViewGroup downloadsLayout;

    @BindView(R.id.youtubeViewMovieTrailerContainer)
    ViewGroup youtubeTrailerContainer;

    @BindView(R.id.banner_container)
    LinearLayout adBannerContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String[] torrentDownloadStrings = new String[3];

    private FirebaseAnalytics mFirebaseAnalytics;
    private Movie movie;
    private String downloadLinksURL = "";
    private AdView adView;

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
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Cannot find any apps in the phone to download this movie. Please install a torrent app and try again.", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);

                snackbar.setAction("Search", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=torrent&c=apps")));
                        } catch (android.content.ActivityNotFoundException anfe) {

                        }
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
        initializeDownloadButtons(movie);
        initializeYoutubeTrailerView();
        initializeBannerAdd();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MovieDetail");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movie.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


    }

    private void initializeBannerAdd() {
        adView = new AdView(this, FB_AD_BANNER_ID, AdSize.BANNER_HEIGHT_90);

        adBannerContainer.addView(adView);

        // Request an ad
        adView.loadAd();
    }

    private void initializeYoutubeTrailerView() {
        if (movie.getYtTrailerCode() != null && !movie.getYtTrailerCode().isEmpty()) {
            YouTubePlayerSupportFragment mYoutubePlayerFragment = new YouTubePlayerSupportFragment();
            mYoutubePlayerFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        youTubePlayer.cueVideo(movie.getYtTrailerCode());
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
            movieGenereRecyclerView.setLayoutManager(layoutManager);
            movieGenereRecyclerView.setHasFixedSize(true);
            movieGenereRecyclerView.setAdapter(movieGenreRecyclerAdapter);
        } else {

        }

    }

    private void initializeDownloadButtons(Movie movie) {
        if (movie.getTorrents() != null && movie.getTorrents().size() > 0) {
            for (Torrent torrent : movie.getTorrents()) {
                if (torrent.getQuality().equalsIgnoreCase("720p")) {
                    hd720pDownload.setVisibility(View.VISIBLE);
                    torrentDownloadStrings[0] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("720p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("1080p")) {
                    fullHD1080pDownload.setVisibility(View.VISIBLE);
                    torrentDownloadStrings[1] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("1080p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("3D")) {
                    threeDDownload.setVisibility(View.VISIBLE);
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
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ShareMovieTorrent");
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


        return movieTitle + "\n\n" + downloadLinks;
    }


    private void generateShareEmailBodyDownloadLinks(String type, String url) {
        String downloadLink = "\t" + type + " - " + url + "\n\n";
        this.downloadLinksURL += downloadLink;
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();

    }
}
