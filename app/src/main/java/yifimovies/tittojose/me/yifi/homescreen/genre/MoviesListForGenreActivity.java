package yifimovies.tittojose.me.yifi.homescreen.genre;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 23-02-2018.
 */

public class MoviesListForGenreActivity extends AppCompatActivity {

    @BindView(R.id.toolbarGenreMovies)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_movies);
        ButterKnife.bind(this);
        String genreType = getIntent().getStringExtra("genre");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(genreType);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GenreMovieListFragment genreMovieListFragment = new GenreMovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("genre", genreType);
        genreMovieListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.layoutGenreMoviesContainer, genreMovieListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
