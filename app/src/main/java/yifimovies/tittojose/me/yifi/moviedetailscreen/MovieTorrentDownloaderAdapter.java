package yifimovies.tittojose.me.yifi.moviedetailscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yifimovies.tittojose.me.yifi.R;
import yifimovies.tittojose.me.yifi.api.model.Torrent;

/**
 * Created by titto.jose on 21-03-2018.
 */

class MovieTorrentDownloaderAdapter extends RecyclerView.Adapter<MovieTorrentDownloaderAdapter.TorrentViewHolder> {

    public interface DownloadTorrentClickListener {
        void onDownloadTorrentClicked(String torrentLink);

        void onDownloadMagnetClicked(String torrentLink);
    }

    private Context context;
    private String movieName;
    private List<Torrent> torrents;
    private DownloadTorrentClickListener downloadTorrentClickListener;
    private int width;

    public MovieTorrentDownloaderAdapter(Context context, String movieName, List<Torrent> torrents, DownloadTorrentClickListener downloadTorrentClickListener, int width) {
        this.context = context;
        this.movieName = movieName;
        this.torrents = torrents;
        this.downloadTorrentClickListener = downloadTorrentClickListener;

        this.width = width;
    }

    @NonNull
    @Override
    public TorrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View downloadItem = inflater.inflate(R.layout.item_download_list, parent, false);

        if (width != 0) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) downloadItem.getLayoutParams();
            layoutParams.width = width;
            downloadItem.setLayoutParams(layoutParams);
        }

        return new MovieTorrentDownloaderAdapter.TorrentViewHolder(downloadItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentViewHolder holder, int position) {
        final Torrent torrent = torrents.get(position);
        holder.title.setText(torrent.getQuality());
        holder.downloadSize.setText(torrent.getSize());
        holder.magnetDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String torrentLink = "magnet:?xt=urn:btih:" + torrent.getHash()
                            + "&dn=" + URLEncoder.encode(movieName, "utf-8") +
                            "&tr=udp%3A%2F%2Fglotorrents.pw%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337";

                    downloadTorrentClickListener.onDownloadMagnetClicked(torrentLink);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });

        holder.torrentDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadTorrentClickListener.onDownloadTorrentClicked(torrent.getUrl());
            }
        });
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

        @BindView(R.id.layoutTorrentDownload)
        ViewGroup torrentDownload;

        @BindView(R.id.layoutMagnetDownload)
        ViewGroup magnetDownload;


        public TorrentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}