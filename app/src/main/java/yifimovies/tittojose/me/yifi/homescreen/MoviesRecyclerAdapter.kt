package yifimovies.tittojose.me.yifi.homescreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface MoviesRecyclerAdapterListener {
        void onItemClickListener(Movie movie, ImageView imageView);
    }

    List<Object> movies;
    Context context;
    MoviesRecyclerAdapterListener listener;

    public static final int MOVIE = 0;

    public MoviesRecyclerAdapter(Context context, List<Object> moviesList, MoviesRecyclerAdapterListener listener) {
        this.movies = moviesList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MOVIE) {
            View movieView = inflater.inflate(R.layout.item_movie_list, parent, false);
            return new MoviesViewHolder(movieView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int itemType = getItemViewType(position);
        if (itemType == MOVIE) {
            final Movie movie = (Movie) movies.get(position);
            final MoviesViewHolder moviesViewHolder = (MoviesViewHolder) holder;
            moviesViewHolder.movieTitle.setText(movie.getTitle() + " - (" + movie.getYear() + ")");
            moviesViewHolder.rating.setText(String.format("%d", (long) movie.getRating()));
            Glide.with(context)
                    .load(movie.getMediumCoverImage())
                    .into(moviesViewHolder.movieImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(movie, moviesViewHolder.movieImage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = movies.get(position);
        if (item instanceof Movie) {
            return MOVIE;
        } else {
            return -1;
        }
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewMovieListItem)
        ImageView movieImage;

        @BindView(R.id.tvTitleMovieListItem)
        TextView movieTitle;

        @BindView(R.id.tvRatingMovieListItem)
        TextView rating;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
