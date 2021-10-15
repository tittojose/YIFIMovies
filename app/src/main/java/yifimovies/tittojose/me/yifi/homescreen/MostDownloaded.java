package yifimovies.tittojose.me.yifi.homescreen;

/**
 * Created by titto.jose on 22-02-2018.
 */

public class MostDownloaded extends MoviesListBaseFragment {

    @Override
    protected void makeMoviesAPICall() {
        moviesService.getMostDownloadedMovies(page, PAGE_SIZE).enqueue(apiCallback);
    }
}