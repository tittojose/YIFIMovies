package yifimovies.tittojose.me.yifi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.api.model.Movie;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MovesSuggestionsRecyclerAdapter extends RecyclerView.Adapter<MovesSuggestionsRecyclerAdapter.MoviesViewHolder> {

    public interface MoviesRecyclerAdapterListener {
        public void onItemClickListener(Movie movie, ImageView imageView);
    }

    List<Movie> movies;
    Context context;
    MoviesRecyclerAdapterListener listener;


    public MovesSuggestionsRecyclerAdapter(Context context, List<Movie> moviesList, MoviesRecyclerAdapterListener listener) {
        this.movies = moviesList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie_suggestion_list, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        Glide.with(context)
                .load(movie.getMediumCoverImage())
                .into(holder.movieImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClickListener(movie, holder.movieImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void updateMoviesList(List<Movie> items) {
        movies = items;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewMovieListItem)
        ImageView movieImage;

        @BindView(R.id.tvTitleMovieListItem)
        TextView movieTitle;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
