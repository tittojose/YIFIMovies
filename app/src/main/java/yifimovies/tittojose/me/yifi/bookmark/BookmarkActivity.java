package yifimovies.tittojose.me.yifi.bookmark;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

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

    @BindView(R.id.recyclerViewBookmarkedMoviesList)
    RecyclerView bookmarkedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        bookmarkedRecyclerView.setHasFixedSize(true);

        List<Object> listBookmarkedMovies = new ArrayList<>();
        listBookmarkedMovies.addAll(BookmarkPrefModel.getListBookmarkedMovies(BookmarkActivity.this));

        MoviesRecyclerAdapter mAdapter = new MoviesRecyclerAdapter(BookmarkActivity.this, listBookmarkedMovies, new MoviesRecyclerAdapter.MoviesRecyclerAdapterListener() {
            @Override
            public void onItemClickListener(Movie movie, ImageView imageView) {
                ActivityOptions options = null;
                Intent i = new Intent(BookmarkActivity.this, MovieDetailActivity.class);
                i.putExtra("movie", movie);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        bookmarkedRecyclerView.setAdapter(mAdapter);
    }
}
