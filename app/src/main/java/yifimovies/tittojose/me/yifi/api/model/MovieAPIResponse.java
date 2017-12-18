package yifimovies.tittojose.me.yifi.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MovieAPIResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("data")
    private MoviesPage data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public MoviesPage getData() {
        return data;
    }

    public void setData(MoviesPage data) {
        this.data = data;
    }
}
