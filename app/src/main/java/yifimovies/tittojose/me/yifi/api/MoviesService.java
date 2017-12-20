package yifimovies.tittojose.me.yifi.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;

/**
 * Created by titto.jose on 14-12-2017.
 */

public interface MoviesService {
    @GET("v2/list_movies.json?sort_by=rating")
    Call<MovieAPIResponse> getTopMovies(@Query("page") int page, @Query("limit") int limit);

    @GET("v2/list_movies.json&sort_by=date_added")
    Call<MovieAPIResponse> getLatestMovies(@Query("page") int page, @Query("limit") int limit);

}
