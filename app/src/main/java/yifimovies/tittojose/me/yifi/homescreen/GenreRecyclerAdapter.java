package yifimovies.tittojose.me.yifi.homescreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;

/**
 * Created by titto.jose on 22-02-2018.
 */

public class GenreRecyclerAdapter extends RecyclerView.Adapter<GenreRecyclerAdapter.ViewHolder> {

    private Context context;
    private String[] genreList;

    public GenreRecyclerAdapter(Context context, String[] genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View genreView = inflater.inflate(R.layout.item_genre_list, parent, false);
        return new GenreRecyclerAdapter.ViewHolder(genreView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.genreTextView.setText(genreList[position]);
    }

    @Override
    public int getItemCount() {
        return genreList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGenreListItem)
        TextView genreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
