package yifimovies.tittojose.me.yifi.bookmark.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.persistance.SharedPrefProvider;

public class BookmarkPrefModel implements Serializable {

    private static final String BOOKMARK_PREF_KEY = "bookmark_shared_key";

    private HashMap<Integer, Movie> movieHashMap;

    public BookmarkPrefModel(HashMap<Integer, Movie> movieHashMap) {
        this.movieHashMap = movieHashMap;
    }

    public static List<Movie> getListBookmarkedMovies(Context context) {
        HashMap<Integer, Movie> movieHashMap = getMovieHashMap(context);
        if (movieHashMap == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(movieHashMap.values());
    }

    public static boolean addMovieToBookmark(Context context, Movie movie) {
        HashMap<Integer, Movie> movieHashMap = getMovieHashMap(context);
        movieHashMap.put(movie.getId(), movie);
        SharedPrefProvider.getInstance(context).writeSharedObjectValue(BOOKMARK_PREF_KEY, new BookmarkPrefModel(movieHashMap));
        return true;
    }

    public static boolean removeMovieToBookmark(Context context, Movie movie) {
        HashMap<Integer, Movie> movieHashMap = getMovieHashMap(context);
        movieHashMap.remove(movie.getId());

        SharedPrefProvider.getInstance(context).writeSharedObjectValue(BOOKMARK_PREF_KEY, new BookmarkPrefModel(movieHashMap));
        return true;
    }

    public static boolean isMovieBookmarked(Context context, Movie movie) {
        HashMap<Integer, Movie> movieHashMap = getMovieHashMap(context);

        return movieHashMap.containsKey(movie.getId());


    }

    public static HashMap<Integer, Movie> getMovieHashMap(Context context) {
        BookmarkPrefModel bookmarkPrefModel = SharedPrefProvider.getInstance(context).getSharedObjectValue(BOOKMARK_PREF_KEY, BookmarkPrefModel.class);
        if (bookmarkPrefModel != null && bookmarkPrefModel.getMovieHashMap() != null) {
            return bookmarkPrefModel.getMovieHashMap();
        }
        return new HashMap<>();
    }


    public HashMap<Integer, Movie> getMovieHashMap() {
        return movieHashMap;
    }
}
