package yifimovies.tittojose.me.yifi.genre;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
