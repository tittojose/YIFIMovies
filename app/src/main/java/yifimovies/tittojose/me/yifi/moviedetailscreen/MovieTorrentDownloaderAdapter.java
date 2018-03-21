package yifimovies.tittojose.me.yifi.moviedetailscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Torrent;

/**
 * Created by titto.jose on 21-03-2018.
 */

class MovieTorrentDownloaderAdapter extends RecyclerView.Adapter<MovieTorrentDownloaderAdapter.TorrentViewHolder> {


    private Context context;
    private List<Torrent> torrents;

    public MovieTorrentDownloaderAdapter(Context context, List<Torrent> torrents) {
        this.context = context;
        this.torrents = torrents;
    }

    @NonNull
    @Override
    public TorrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View downloadItem = inflater.inflate(R.layout.item_download_list, parent, false);
        return new MovieTorrentDownloaderAdapter.TorrentViewHolder(downloadItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentViewHolder holder, int position) {
        Torrent torrent = torrents.get(position);
        holder.title.setText(torrent.getQuality());
        holder.downloadSize.setText(torrent.getSize());
    }

    @Override
    public int getItemCount() {
        return torrents.size();
    }

    public class TorrentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDownloadItemTitle)
        TextView title;

        @BindView(R.id.tvDownloadItemSize)
        TextView downloadSize;

        public TorrentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
