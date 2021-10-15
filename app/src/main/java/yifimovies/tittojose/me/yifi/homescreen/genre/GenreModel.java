package yifimovies.tittojose.me.yifi.homescreen.genre;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;


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
        genreModelList.add(new GenreModel("Action", 1));
        genreModelList.add(new GenreModel("Adventure", 1));
        genreModelList.add(new GenreModel("Animation", 1));
        genreModelList.add(new GenreModel("Biography", 1));
        genreModelList.add(new GenreModel("Comedy", 1));
        genreModelList.add(new GenreModel("Crime", 1));
        genreModelList.add(new GenreModel("Documentary", 1));
        genreModelList.add(new GenreModel("Drama", 1));
        genreModelList.add(new GenreModel("Family", 1));
        genreModelList.add(new GenreModel("Fantasy", 1));
//        genreModelList.add(new GenreModel("Film Noir", R.drawable.ic_genre_action_large));
        genreModelList.add(new GenreModel("History", 1));
        genreModelList.add(new GenreModel("Horror", 1));
        genreModelList.add(new GenreModel("Music", 1));
        genreModelList.add(new GenreModel("Musical", 1));
        genreModelList.add(new GenreModel("Mystery", 1));
        genreModelList.add(new GenreModel("Romance", 1));
        genreModelList.add(new GenreModel("Sci-Fi", 1));
//        genreModelList.add(new GenreModel("Short", R.drawable.ic_genre_mystery_large));
        genreModelList.add(new GenreModel("Sport", 1));
//        genreModelList.add(new GenreModel("Superhero", R.drawable.ic_genre_animation));
        genreModelList.add(new GenreModel("Thriller", 1));
        genreModelList.add(new GenreModel("War", 1));
        genreModelList.add(new GenreModel("Western", 1));
        return genreModelList;
    }
}
