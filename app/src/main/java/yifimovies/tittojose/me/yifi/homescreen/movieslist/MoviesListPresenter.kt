package yifimovies.tittojose.me.yifi.homescreen.movieslist

import yifimovies.tittojose.me.yifi.repositories.MoviesRepository

/**
 * Created by titto.jose on 05-03-2018.
 */

class MoviesListPresenter(private var moviesListView: MoviesListView, private var movieRepositories: MoviesRepository) {

    fun loadMovies() {
        val moviesList = movieRepositories.getListOfMovies()
        if ( moviesList.isNotEmpty()) {
            moviesListView.displayMoviesList(moviesList)
        } else {
            moviesListView.displayErrorWhenNoMoviesLoaded("")
        }
    }
}