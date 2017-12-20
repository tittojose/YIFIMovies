package yifimovies.tittojose.me.yifi;

/**
 * Created by titto.jose on 21-12-2017.
 */

public class LatestMoviesFragment extends MoviesListBaseFragment {
    @Override
    protected void makeMoviesAPICall() {
        moviesService.getLatestMovies(page, PAGE_SIZE).enqueue(apiCallback);
    }
}
