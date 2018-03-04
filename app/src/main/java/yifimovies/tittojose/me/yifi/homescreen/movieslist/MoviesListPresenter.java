package yifimovies.tittojose.me.yifi.homescreen.movieslist;

import java.util.List;

import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.repositories.MovieRepositories;

/**
 * Created by titto.jose on 28-02-2018.
 */

public class MoviesListPresenter {
    private MoviesListView moviesListView;
    private MovieRepositories mockMovieRepositories;

    public MoviesListPresenter(MoviesListView moviesListView, MovieRepositories mockMovieRepositories) {
        this.moviesListView = moviesListView;
        this.mockMovieRepositories = mockMovieRepositories;
    }

    public void loadMovies() {
        List<Movie> movieList = mockMovieRepositories.getListOfMovies();
        if (movieList != null && movieList.size() > 0) {
            moviesListView.displayMoviesList(movieList);
        } else {
            moviesListView.displayErrorWhenNoMoviesLoaded("");
        }
    }
}
