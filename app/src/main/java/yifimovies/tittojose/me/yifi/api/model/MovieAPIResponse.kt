package yifimovies.tittojose.me.yifi.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import yifimovies.tittojose.me.yifi.api.model.MoviesPage

/**
 * Created by titto.jose on 14-12-2017.
 */
@Parcelize
class MovieAPIResponse(
        @SerializedName("status")
        var status: String? = null,

        @SerializedName("status_message")
        var statusMessage: String? = null,

        @SerializedName("data")
        var data: MoviesPage? = null
) : Parcelable