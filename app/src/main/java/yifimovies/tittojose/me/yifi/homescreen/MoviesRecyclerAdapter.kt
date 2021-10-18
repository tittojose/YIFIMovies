package yifimovies.tittojose.me.yifi.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_list.view.*
import yifimovies.tittojose.me.yifi.R
import yifimovies.tittojose.me.yifi.api.model.Movie
import yifimovies.tittojose.me.yifi.homescreen.MoviesRecyclerAdapter.MoviesViewHolder

/**
 * Created by titto.jose on 14-12-2017.
 */
class MoviesRecyclerAdapter(
        val movies: List<Movie>,
        var listener: MoviesRecyclerAdapterListener
) : RecyclerView.Adapter<MoviesViewHolder>() {
    interface MoviesRecyclerAdapterListener {
        fun onItemClickListener(movie: Movie?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val movieView = inflater.inflate(R.layout.item_movie_list, parent, false)
        val moviesViewHolder = MoviesViewHolder(movieView)
        movieView.setOnClickListener {
            val movie = movies[moviesViewHolder.absoluteAdapterPosition]
            listener.onItemClickListener(movie)
        }
        return moviesViewHolder
    }

    override fun onBindViewHolder(moviesViewHolder: MoviesViewHolder, position: Int) =
            moviesViewHolder.bind(movies[position])

    override fun getItemCount(): Int = movies.size

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.tvTitleMovieListItem.text = movie.title + " - (" + movie.year + ")"
            itemView.tvRatingMovieListItem.text = String.format("%d", movie.rating.toLong())
            Glide.with(itemView.context)
                    .load(movie.mediumCoverImage)
                    .into(itemView.imageViewMovieListItem)
        }
    }
}
