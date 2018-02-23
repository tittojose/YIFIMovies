package yifimovies.tittojose.me.yifi.homescreen.genre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 22-02-2018.
 */

public class GenreListFragment extends Fragment {

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

    private static final int[] genreIconArray = {

    };

    @BindView(R.id.rvGenreList)
    RecyclerView genreRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genre_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        genreRecyclerView.setLayoutManager(linearLayoutManager);
        genreRecyclerView.setHasFixedSize(true);
        GenreRecyclerAdapter genreRecyclerAdapter = new GenreRecyclerAdapter(getActivity(), genreArray);
        genreRecyclerView.setAdapter(genreRecyclerAdapter);
    }
}
