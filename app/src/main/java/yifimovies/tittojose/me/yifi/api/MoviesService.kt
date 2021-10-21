package yifimovies.tittojose.me.yifi.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse

/**
 * Created by titto.jose on 14-12-2017.
 */
interface MoviesService {

    companion object{
        const val PRIMARY_API_END_POINT = "https://yts.am/api/"
    }
    @GET("v2/list_movies.json")
    fun getMovieList(@Query("sort_by") type: String?, @Query("page") page: Int, @Query("limit") limit: Int): Call<MovieAPIResponse?>

    @GET("v2/list_movies.json")
    suspend fun fetchMovieList(
            @Query("sort_by") type: String?,
            @Query("page") page: Int,
            @Query("limit") limit: Int
    ): MovieAPIResponse

    @GET("v2/list_movies.json?sort_by=date_added")
    fun getMoviesForGenre(@Query("genre") genre: String?, @Query("page") page: Int, @Query("limit") limit: Int): Call<MovieAPIResponse?>?

    @GET("v2/list_movies.json?sort_by=date_added&page=1&limit=50")
    fun getMovieSuggestions(@Query("query_term") query: String?): Call<MovieAPIResponse?>?
}