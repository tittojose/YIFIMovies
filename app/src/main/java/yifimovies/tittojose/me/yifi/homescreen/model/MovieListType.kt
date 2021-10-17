package yifimovies.tittojose.me.yifi.homescreen.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class MovieListType(val query: String) : Parcelable {
    @Parcelize
    object LATEST : MovieListType("date_added")

    @Parcelize
    object MOST_DOWNLOADED : MovieListType("download_count")

    @Parcelize
    object TOP : MovieListType("rating")
}