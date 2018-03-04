package yifimovies.tittojose.me.yifi.homescreen.movieslist;

import java.util.List;

import yifimovies.tittojose.me.yifi.api.model.Movie;

/**
 * Created by titto.jose on 28-02-2018.
 */

public interface MoviesListView {

    void displayMoviesList(List<Movie> movies);

    void displayErrorWhenNoMoviesLoaded(String errorMessage);
}
