package yifimovies.tittojose.me.yifi.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import yifimovies.tittojose.me.yifi.api.model.Torrent
import java.io.Serializable

/**
 * Created by titto.jose on 14-12-2017.
 */
@Parcelize
data class Movie(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("url")
        val url: String? = null,

        @SerializedName("imdb_code")
        val imdbCode: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("title_english")
        val titleEnglish: String? = null,

        @SerializedName("title_long")
        val titleLong: String? = null,

        @SerializedName("slug")
        val slug: String? = null,

        @SerializedName("year")
        val year: Int? = null,

        @SerializedName("rating")
        val rating: Float = 0f,

        @SerializedName("runtime")
        val runtime: Int? = null,

        @SerializedName("genres")
        val genres: List<String>? = null,

        @SerializedName("summary")
        val summary: String? = null,

        @SerializedName("description_full")
        val descriptionFull: String? = null,

        @SerializedName("synopsis")
        val synopsis: String? = null,

        @SerializedName("yt_trailer_code")
        val ytTrailerCode: String? = null,

        @SerializedName("language")
        val language: String? = null,

        @SerializedName("mpa_rating")
        val mpaRating: String? = null,

        @SerializedName("background_image")
        val backgroundImage: String? = null,

        @SerializedName("background_image_original")
        val backgroundImageOriginal: String? = null,

        @SerializedName("small_cover_image")
        val smallCoverImage: String? = null,

        @SerializedName("medium_cover_image")
        val mediumCoverImage: String? = null,

        @SerializedName("large_cover_image")
        val largeCoverImage: String? = null,

        @SerializedName("state")
        val state: String? = null,

        @SerializedName("date_uploaded")
        val dateUploaded: String? = null,

        @SerializedName("date_uploaded_unix")
        val dateUploadedUnix: Int? = null,

        @SerializedName("torrents")
        val torrents: List<Torrent>? = null
) : Parcelable
