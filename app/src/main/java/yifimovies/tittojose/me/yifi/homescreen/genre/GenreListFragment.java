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

    @BindView(R.id.rvGenreList)
    RecyclerView genreRecyclerView;
    private LinearLayoutManager linearLayoutManager;

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
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        genreRecyclerView.setLayoutManager(linearLayoutManager);
        GenreRecyclerAdapter genreRecyclerAdapter = new GenreRecyclerAdapter(getActivity());
        genreRecyclerView.setAdapter(genreRecyclerAdapter);

    }
}
