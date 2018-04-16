package yifimovies.tittojose.me.yifi.homescreen.genre;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 22-02-2018.
 */

public class GenreRecyclerAdapter extends RecyclerView.Adapter<GenreRecyclerAdapter.ViewHolder> {

    private final OnGenreListItemClickListener genreListItemClickListener;

    public interface OnGenreListItemClickListener {
        void onGenreItemClicked(GenreModel genreModel);
    }

    private Context context;
    private List<GenreModel> genreModelList;

    public GenreRecyclerAdapter(Context context, OnGenreListItemClickListener genreListItemClickListener) {
        this.context = context;
        genreModelList = GenreModel.getGenreModelList();
        this.genreListItemClickListener = genreListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View genreView = inflater.inflate(R.layout.item_genre_list, parent, false);
        return new GenreRecyclerAdapter.ViewHolder(genreView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.genreTextView.setText(genreModelList.get(position).getGenreName());
//        holder.genreImageView.setImageResource(genreModelList.get(position).getGenreIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreListItemClickListener.onGenreItemClicked(genreModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGenreListItem)
        TextView genreTextView;

        @BindView(R.id.imgViewGenreIcon)
        ImageView genreImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
