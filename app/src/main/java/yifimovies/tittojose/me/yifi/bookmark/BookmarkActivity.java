package yifimovies.tittojose.me.yifi.bookmark;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.bookmark.model.BookmarkPrefModel;
import yifimovies.tittojose.me.yifi.ui.home.MoviesRecyclerAdapter;
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity;

public class BookmarkActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewBookmarkedMoviesList)
    RecyclerView bookmarkedRecyclerView;

    @BindView(R.id.toolbarBookmarkedMovies)
    Toolbar toolbar;

    @BindView(R.id.tveEmptyBookmark)
    TextView emptyBookmark;

    private MoviesRecyclerAdapter mAdapter;
    private List<Movie> listBookmarkedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();

        listBookmarkedMovies = new ArrayList<>();
        listBookmarkedMovies.addAll(BookmarkPrefModel.getListBookmarkedMovies(BookmarkActivity.this));

        if (listBookmarkedMovies.size() > 0) {

            bookmarkedRecyclerView.setVisibility(View.VISIBLE);
            emptyBookmark.setVisibility(View.GONE);

            mAdapter = new MoviesRecyclerAdapter(listBookmarkedMovies, new MoviesRecyclerAdapter.MoviesRecyclerAdapterListener() {
                @Override
                public void onItemClickListener(@Nullable Movie movie) {
                    ActivityOptions options = null;
                    Intent i = new Intent(BookmarkActivity.this, MovieDetailActivity.class);
                    i.putExtra("movie", movie);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            });

            final GridLayoutManager layoutManager = new GridLayoutManager(BookmarkActivity.this, 2);

            bookmarkedRecyclerView.setLayoutManager(layoutManager);
            bookmarkedRecyclerView.setHasFixedSize(true);

            bookmarkedRecyclerView.setAdapter(mAdapter);
        } else {
            bookmarkedRecyclerView.setVisibility(View.GONE);
            emptyBookmark.setVisibility(View.VISIBLE);
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
