package yifimovies.tittojose.me.yifi.homescreen;

/**
 * Created by titto.jose on 21-12-2017.
 */

public class LatestMoviesFragment extends MoviesListBaseFragment {


    @Override
    protected void makeMoviesAPICall() {
        moviesService.getLatestMovies(page, PAGE_SIZE).enqueue(apiCallback);
    }

    @Override
    protected void setAdPlacementId() {
        AD_PLACEMENT_ID = "334553013694096_334884876994243";
    }
}
