package yifimovies.tittojose.me.yifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.florent37.glidepalette.GlidePalette;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.api.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {


    @BindView(R.id.imageViewMovieTitleImage)
    ImageView movieTitleImage;

    @BindView(R.id.tvMovieTitle)
    TextView movieTitleText;

    @BindView(R.id.tvMovieDescription)
    TextView movieDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        try {
            Glide.with(MovieDetailActivity.this)
                    .load(movie.getMediumCoverImage())
                    .listener(GlidePalette.with(movie.getLargeCoverImage())
                            .use(GlidePalette.Profile.MUTED_DARK)
                            .intoBackground(movieTitleText)
                            .intoTextColor(movieTitleText)

                            .use(GlidePalette.Profile.VIBRANT)
                            .intoBackground(movieTitleText, GlidePalette.Swatch.RGB)
                            .intoTextColor(movieTitleText, GlidePalette.Swatch.BODY_TEXT_COLOR)
                            .crossfade(true)
                    )
                    .apply(new RequestOptions().centerCrop())
                    .into(movieTitleImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        movieTitleText.setText(movie.getTitle());
        movieDescriptionText.setText(movie.getDescriptionFull());
    }
}
