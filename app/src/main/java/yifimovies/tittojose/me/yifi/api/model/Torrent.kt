package yifimovies.tittojose.me.yifi.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by titto.jose on 20-12-2017.
 */
@Parcelize
class Torrent(
        @SerializedName("url")
        var url: String? = null,

        @SerializedName("hash")
        var hash: String? = null,

        @SerializedName("quality")
        var quality: String? = null,

        @SerializedName("seeds")
        var seeds: Int? = null,

        @SerializedName("peers")
        var peers: Int? = null,

        @SerializedName("size")
        var size: String? = null,

        @SerializedName("date_uploaded")
        var dateUploaded: String? = null,

        @SerializedName("date_uploaded_unix")
        var dateUploadedUnix: Int? = null
) : Parcelable