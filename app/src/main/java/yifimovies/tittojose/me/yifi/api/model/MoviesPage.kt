package yifimovies.tittojose.me.yifi.api.model

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import yifimovies.tittojose.me.yifi.api.model.MoviesPage

/**
 * Created by titto.jose on 14-12-2017.
 */
@Parcelize
class MoviesPage(
        @SerializedName("movie_count")
        var movieCount: Int? = null,

        @SerializedName("limit")
        var limit: Int? = null,

        @SerializedName("page_number")
        var pageNumber: Int? = null,

        @SerializedName("movies")
        var movies: List<Movie>? = null

) : Parcelable