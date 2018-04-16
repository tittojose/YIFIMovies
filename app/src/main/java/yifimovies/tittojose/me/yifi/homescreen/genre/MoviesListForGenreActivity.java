package yifimovies.tittojose.me.yifi.homescreen.genre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
