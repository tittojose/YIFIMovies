package yifimovies.tittojose.me.yifi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.api.model.Torrent;

public class MovieDetailActivity extends AppCompatActivity {


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
//
//    @BindView(R.id.tvMovieYear)
//    TextView movieYearTextView;

    String[] torrentDownloadStrings = new String[3];

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

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(url), "application/x-bittorrent");
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

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
//        movieYearTextView.setText(String.format("%d",(long) movie.getYear()));
        initializeDownloadButtons(movie);
    }

    private void initializeDownloadButtons(Movie movie) {
        for (Torrent torrent : movie.getTorrents()) {
            if (torrent.getQuality().equalsIgnoreCase("720p")) {
                hd720pDownload.setVisibility(View.VISIBLE);
                torrentDownloadStrings[0] = torrent.getUrl();
            } else if (torrent.getQuality().equalsIgnoreCase("1080p")) {
                fullHD1080pDownload.setVisibility(View.VISIBLE);
                torrentDownloadStrings[1] = torrent.getUrl();
            } else if (torrent.getQuality().equalsIgnoreCase("3D")) {
                threeDDownload.setVisibility(View.VISIBLE);
                torrentDownloadStrings[2] = torrent.getUrl();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
