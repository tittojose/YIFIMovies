package yifimovies.tittojose.me.yifi.genre;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.homescreen.ViewPagerAdapter;
import yifimovies.tittojose.me.yifi.homescreen.genre.GenreModel;

public class GenreActivity extends AppCompatActivity {

    @BindView(R.id.tabsGenre)
    TabLayout tabLayout;

    @BindView(R.id.viewpagerGenre)
    ViewPager viewPager;
    private List<GenreModel> genreModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        ButterKnife.bind(this);
        genreModelList = GenreModel.getGenreModelList();
        setupViewPager();
    }


    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (GenreModel genreModel : genreModelList) {

            GenreMovieListFragmentForTabs genreMovieListFragment = new GenreMovieListFragmentForTabs();
            Bundle bundle = new Bundle();
            bundle.putString("genre", genreModel.getGenreName());
            genreMovieListFragment.setArguments(bundle);

            adapter.addFragment(genreMovieListFragment, genreModel.getGenreName());
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
