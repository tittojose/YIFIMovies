package yifimovies.tittojose.me.yifi.data

import yifimovies.tittojose.me.yifi.api.MoviesService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
        private val moviesService: MoviesService
) {

}