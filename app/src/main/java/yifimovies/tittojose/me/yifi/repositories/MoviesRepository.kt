package yifimovies.tittojose.me.yifi.repositories

import yifimovies.tittojose.me.yifi.api.model.Movie

/**
 * Created by titto.jose on 05-03-2018.
 */
interface MoviesRepository {
    fun getListOfMovies(): List<Movie>
}