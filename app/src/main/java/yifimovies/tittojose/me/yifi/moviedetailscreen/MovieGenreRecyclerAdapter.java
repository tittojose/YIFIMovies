package yifimovies.tittojose.me.yifi.moviedetailscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MovieGenreRecyclerAdapter extends RecyclerView.Adapter<MovieGenreRecyclerAdapter.MovieGenreHolder> {

    List<String> genres;
    Context context;

    public MovieGenreRecyclerAdapter(Context context, List<String> genres) {
        this.context = context;
        this.genres = genres;
    }

    @Override
    public MovieGenreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieGenre = inflater.inflate(R.layout.item_movie_genre, parent, false);
        return new MovieGenreHolder(movieGenre);
    }

    @Override
    public void onBindViewHolder(final MovieGenreHolder holder, int position) {
        final String genre = genres.get(position);
        holder.genreTextView.setText(genre);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }


    class MovieGenreHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGenreMovieListItem)
        TextView genreTextView;

        MovieGenreHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
