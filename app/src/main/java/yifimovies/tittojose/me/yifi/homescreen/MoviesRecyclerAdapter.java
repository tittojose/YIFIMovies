package yifimovies.tittojose.me.yifi.homescreen;

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
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Movie;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder> {

    public interface MoviesRecyclerAdapterListener {
        void onItemClickListener(Movie movie, ImageView imageView);
    }

    List<Movie> movies;
    Context context;
    MoviesRecyclerAdapterListener listener;


    public MoviesRecyclerAdapter(Context context, List<Movie> moviesList, MoviesRecyclerAdapterListener listener) {
        this.movies = moviesList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie_list, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        holder.movieTitle.setText(movie.getTitle());
        holder.rating.setText(String.format("%d",(long) movie.getRating()));
        Glide.with(context)
                .load(movie.getMediumCoverImage())
                .into(holder.movieImage);

//        try {
//            Glide.with(context).load(movie.getMediumCoverImage())
//                    .listener(GlidePalette.with(movie.getLargeCoverImage())
//                            .use(GlidePalette.Profile.MUTED_DARK)
//                            .intoBackground(holder.movieTitle, GlidePalette.Swatch.RGB)
//                            .intoTextColor(holder.movieTitle, GlidePalette.Swatch.TITLE_TEXT_COLOR)
//                            .crossfade(true)
//                    )
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .apply(new RequestOptions().centerCrop())
//                    .into(holder.movieImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(movie, holder.movieImage);
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

        @BindView(R.id.tvRatingMovieListItem)
        TextView rating;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
