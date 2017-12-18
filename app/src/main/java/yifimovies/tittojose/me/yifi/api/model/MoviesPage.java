package yifimovies.tittojose.me.yifi.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MoviesPage {

    @SerializedName("movie_count")
    private Integer movieCount;

    @SerializedName("limit")
    private Integer limit;

    @SerializedName("page_number")
    private Integer pageNumber;

    @SerializedName("movies")
    private List<Movie> movies = null;

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
