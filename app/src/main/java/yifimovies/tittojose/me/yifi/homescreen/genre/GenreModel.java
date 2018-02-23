package yifimovies.tittojose.me.yifi.homescreen.genre;

import android.support.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 23-02-2018.
 */

public class GenreModel {

    private static final String[] genreArray = {
            "Comedy",
            "Sci-Fi",
            "Horror",
            "Romance",
            "Action",
            "Thriller",
            "Drama",
            "Mystery",
            "Crime",
            "Animation",
            "Adventure",
            "Fantasy",
            "Comedy-Romance",
            "Action-Comedy",
            "SuperHero"
    };

    String genreName;

    @DrawableRes
    int genreIcon;

    public GenreModel(String genreName, int genreIcon) {
        this.genreName = genreName;
        this.genreIcon = genreIcon;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getGenreIcon() {
        return genreIcon;
    }

    public void setGenreIcon(int genreIcon) {
        this.genreIcon = genreIcon;
    }

    public static List<GenreModel> getGenreModelList() {
        List<GenreModel> genreModelList = new ArrayList<>();
        genreModelList.add(new GenreModel("Comedy", R.drawable.ic_genre_comedy_large));
        genreModelList.add(new GenreModel("Sci-Fi", R.drawable.ic_genre_scifi_large));
        genreModelList.add(new GenreModel("Horror", R.drawable.ic_genre_horror_large));
        genreModelList.add(new GenreModel("Romance", R.drawable.ic_genre_romance_large));
        genreModelList.add(new GenreModel("Action", R.drawable.ic_genre_action_large));
        genreModelList.add(new GenreModel("Thriller", R.drawable.ic_genre_thriller_large));
        genreModelList.add(new GenreModel("Drama", R.drawable.ic_genre_drama_large));
        genreModelList.add(new GenreModel("Mystery", R.drawable.ic_genre_mystery_large));
        genreModelList.add(new GenreModel("Crime", R.drawable.ic_genre_crime_large));
        genreModelList.add(new GenreModel("Animation", R.drawable.ic_genre_animation));
        genreModelList.add(new GenreModel("Adventure", R.drawable.ic_genre_adventure_large));
        genreModelList.add(new GenreModel("Fantasy", R.drawable.ic_genre_fantasy_large));
//        genreModelList.add(new GenreModel("Comedy-Romance", R.drawable.ic_genre_comedy_large));
//        genreModelList.add(new GenreModel("Action-Comedy", R.drawable.ic_genre_action_large));
//        genreModelList.add(new GenreModel("SuperHero", R.drawable.ic_genre_history_large));
        return genreModelList;
    }
}
