package yifimovies.tittojose.me.yifi.homescreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
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
    public static final int NATIVE_AD = 1;


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
        } else if (viewType == NATIVE_AD) {
            View nativeAdItem = inflater.inflate(R.layout.item_native_ad, parent, false);
            return new NativeAdViewHolder(nativeAdItem);
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
            moviesViewHolder.movieTitle.setText(movie.getTitle());
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
        } else if (itemType == NATIVE_AD) {
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
            NativeAd nativeAd = (NativeAd) movies.get(position);

            ImageView adImage = nativeAdViewHolder.adImage;
            TextView tvAdTitle = nativeAdViewHolder.tvAdTitle;
            TextView tvAdBody = nativeAdViewHolder.tvAdBody;
            Button btnCTA = nativeAdViewHolder.btnCTA;
            LinearLayout adChoicesContainer = nativeAdViewHolder.adChoicesContainer;
            MediaView mediaView = nativeAdViewHolder.mediaView;

            tvAdTitle.setText(nativeAd.getAdTitle());
            tvAdBody.setText(nativeAd.getAdBody());
            NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), adImage);
            btnCTA.setText(nativeAd.getAdCallToAction());
            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
            adChoicesContainer.addView(adChoicesView);
            mediaView.setNativeAd(nativeAd);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(adImage);
            clickableViews.add(btnCTA);
            clickableViews.add(mediaView);
            nativeAd.registerViewForInteraction(nativeAdViewHolder.container, clickableViews);
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
        } else if (item instanceof Ad) {
            return NATIVE_AD;
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

    private static class NativeAdViewHolder extends RecyclerView.ViewHolder {
        ImageView adImage;
        TextView tvAdTitle;
        TextView tvAdBody;
        Button btnCTA;
        View container;
        LinearLayout adChoicesContainer;
        MediaView mediaView;

        NativeAdViewHolder(View itemView) {
            super(itemView);
            this.container = itemView;
            adImage = itemView.findViewById(R.id.adImage);
            tvAdTitle = itemView.findViewById(R.id.tvAdTitle);
            tvAdBody = itemView.findViewById(R.id.tvAdBody);
            btnCTA = itemView.findViewById(R.id.btnCTA);
            adChoicesContainer = itemView.findViewById(R.id.adChoicesContainer);
            mediaView = itemView.findViewById(R.id.mediaView);
        }
    }

}
