package yifimovies.tittojose.me.yifi.genre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yifimovies.tittojose.me.yifi.homescreen.MoviesListBaseFragment;

/**
 * Created by titto.jose on 23-02-2018.
 */

public class GenreMovieListFragmentForTabs extends MoviesListBaseFragment {

    String genreType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        genreType = getArguments().getString("genre");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void makeMoviesAPICall() {
        moviesService.getMoviesForGenre(genreType, page, PAGE_SIZE).enqueue(apiCallback);
    }

    @Override
    protected void setAdPlacementId() {
        AD_PLACEMENT_ID = "334553013694096_361205744362156";
    }
}