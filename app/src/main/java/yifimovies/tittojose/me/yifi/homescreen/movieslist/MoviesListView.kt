package yifimovies.tittojose.me.yifi.homescreen.movieslist

import yifimovies.tittojose.me.yifi.api.model.Movie

/**
 * Created by titto.jose on 05-03-2018.
 */
interface MoviesListView {

    fun displayMoviesList(movies: List<Movie>)

    fun displayErrorWhenNoMoviesLoaded(errorMessage: String)
}