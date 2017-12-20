package yifimovies.tittojose.me.yifi.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by titto.jose on 20-12-2017.
 */

public class Torrent implements Serializable {
    @SerializedName("url")
    private String url;

    @SerializedName("hash")
    private String hash;

    @SerializedName("quality")
    private String quality;

    @SerializedName("seeds")
    private Integer seeds;

    @SerializedName("peers")
    private Integer peers;

    @SerializedName("size")
    private String size;

    @SerializedName("date_uploaded")
    private String dateUploaded;

    @SerializedName("date_uploaded_unix")
    private Integer dateUploadedUnix;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Integer getSeeds() {
        return seeds;
    }

    public void setSeeds(Integer seeds) {
        this.seeds = seeds;
    }

    public Integer getPeers() {
        return peers;
    }

    public void setPeers(Integer peers) {
        this.peers = peers;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Integer getDateUploadedUnix() {
        return dateUploadedUnix;
    }

    public void setDateUploadedUnix(Integer dateUploadedUnix) {
        this.dateUploadedUnix = dateUploadedUnix;
    }
}
