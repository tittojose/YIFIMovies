package yifimovies.tittojose.me.yifi.moviedetailscreen;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yifimovies.tittojose.me.yifi.Constants;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.Torrent;
import yifimovies.tittojose.me.yifi.bookmark.model.BookmarkPrefModel;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static String YOUTUBE_API_KEY = "AIzaSyBcdx13v4s97QAxxM921ka4WGfCt_91CCU";

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

    @BindView(R.id.btnBookmark)
    LikeButton bookmarkButton;

    @BindView(R.id.layoutBookmark)
    LinearLayout bookmarkLayout;

    String[] torrentDownloadStrings = new String[3];

    private FirebaseAnalytics mFirebaseAnalytics;
    private Movie movie;
    private String downloadLinksURL = "";
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
        movieRatingTextView.setText(movie.getRating() + "");
        initializeMovieGenreList(movie.getGenres());
        initializeDownloadList(movie.getTorrents());
        initializeDownloadButtons(movie);
        initializeYoutubeTrailerView();
        initializeBookmarkState();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MovieDetail");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MovieDetail");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void initializeBookmarkState() {
        bookmarkButton.setLiked(BookmarkPrefModel.isMovieBookmarked(MovieDetailActivity.this, movie));
        bookmarkButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add_Bookmark");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Add_Bookmark");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                BookmarkPrefModel.addMovieToBookmark(MovieDetailActivity.this, movie);
                Snackbar.make(getWindow().getDecorView() //app context can not cast in activity
                        .findViewById(android.R.id.content), "Movie added to your favorites list.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Remove_Bookmark");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Remove_Bookmark");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                BookmarkPrefModel.removeMovieToBookmark(MovieDetailActivity.this, movie);
                Snackbar.make(getWindow().getDecorView() //app context can not cast in activity
                        .findViewById(android.R.id.content), "Movie removed from favorites list.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

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
        if (genres != null) {
            genres.size();
            MovieGenreRecyclerAdapter movieGenreRecyclerAdapter = new MovieGenreRecyclerAdapter(MovieDetailActivity.this, genres);
            final GridLayoutManager layoutManager = new GridLayoutManager(MovieDetailActivity.this, 3);
            movieGenreRecyclerView.setLayoutManager(layoutManager);
            movieGenreRecyclerView.setHasFixedSize(true);
            movieGenreRecyclerView.setAdapter(movieGenreRecyclerAdapter);
        }
    }


    private void initializeDownloadList(List<Torrent> torrents) {
        if (torrents != null) {
            torrents.size();
            int width = getWidthOfTorrentItem(torrents);
            MovieTorrentDownloaderAdapter movieGenreRecyclerAdapter = new MovieTorrentDownloaderAdapter(MovieDetailActivity.this, movie.getTitle(), torrents, new MovieTorrentDownloaderAdapter.DownloadTorrentClickListener() {
                @Override
                public void onDownloadTorrentClicked(String torrentLink, String quality) {
                    try {
                        //Analytics
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "DownloadTorrentClicked_" + quality);
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "DownloadTorrentClicked_" + quality);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.parse(torrentLink), "application/x-bittorrent");
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                    } catch (Exception e) {
                        launchTorrentAppSearch();
                    }
                }

                @Override
                public void onDownloadMagnetClicked(String torrentLink, String quality) {
                    try {
                        //Analytics
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getTitle());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "onDownloadMagnetClicked_" + quality);
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "onDownloadMagnetClicked_" + quality);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(torrentLink)));
                    } catch (Exception e) {
                        launchTorrentAppSearch();
                    }
                }
            }, width);
            movieDownloadRecyclerView.setHasFixedSize(true);
            movieDownloadRecyclerView.setAdapter(movieGenreRecyclerAdapter);
        }
    }

    private int getWidthOfTorrentItem(List<Torrent> torrents) {
        if (torrents.size() == 2) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            try {
                display.getRealSize(size);
            } catch (NoSuchMethodError err) {
                display.getSize(size);
            }
            return (size.x / 2);
        } else {
            return 0;
        }
    }

    private void launchTorrentAppSearch() {
        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.trorrent_app_redirect_message, Snackbar.LENGTH_LONG);
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
                    torrentDownloadStrings[0] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("720p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("1080p")) {
                    torrentDownloadStrings[1] = torrent.getUrl();
                    generateShareEmailBodyDownloadLinks("1080p", torrent.getUrl());
                } else if (torrent.getQuality().equalsIgnoreCase("3D")) {
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
}
