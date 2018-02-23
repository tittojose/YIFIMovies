package yifimovies.tittojose.me.yifi.homescreen;

/**
 * Created by titto.jose on 22-02-2018.
 */

public class MostDownloaded extends MoviesListBaseFragment {


    @Override
    protected void makeMoviesAPICall() {
        moviesService.getMostDownloadedMovies(page, PAGE_SIZE).enqueue(apiCallback);
    }

    @Override
    protected void setAdPlacementId() {
        AD_PLACEMENT_ID = "334553013694096_334884876994243";
    }
}