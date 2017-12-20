package yifimovies.tittojose.me.yifi.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class Movie implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("url")
    private String url;

    @SerializedName("imdb_code")
    private String imdbCode;

    @SerializedName("title")
    private String title;

    @SerializedName("title_english")
    private String titleEnglish;

    @SerializedName("title_long")
    private String titleLong;

    @SerializedName("slug")
    private String slug;

    @SerializedName("year")
    private Integer year;

    @SerializedName("rating")
    private float rating;

    @SerializedName("runtime")
    private Integer runtime;

    @SerializedName("genres")
    private List<String> genres = null;

    @SerializedName("summary")
    private String summary;

    @SerializedName("description_full")
    private String descriptionFull;

    @SerializedName("synopsis")
    private String synopsis;

    @SerializedName("yt_trailer_code")
    private String ytTrailerCode;

    @SerializedName("language")
    private String language;

    @SerializedName("mpa_rating")
    private String mpaRating;

    @SerializedName("background_image")
    private String backgroundImage;

    @SerializedName("background_image_original")
    private String backgroundImageOriginal;

    @SerializedName("small_cover_image")
    private String smallCoverImage;

    @SerializedName("medium_cover_image")
    private String mediumCoverImage;

    @SerializedName("large_cover_image")
    private String largeCoverImage;

    @SerializedName("state")
    private String state;

    @SerializedName("date_uploaded")
    private String dateUploaded;

    @SerializedName("date_uploaded_unix")
    private Integer dateUploadedUnix;

    @SerializedName("torrents")
    private List<Torrent> torrents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getYtTrailerCode() {
        return ytTrailerCode;
    }

    public void setYtTrailerCode(String ytTrailerCode) {
        this.ytTrailerCode = ytTrailerCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public void setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundImageOriginal() {
        return backgroundImageOriginal;
    }

    public void setBackgroundImageOriginal(String backgroundImageOriginal) {
        this.backgroundImageOriginal = backgroundImageOriginal;
    }

    public String getSmallCoverImage() {
        return smallCoverImage;
    }

    public void setSmallCoverImage(String smallCoverImage) {
        this.smallCoverImage = smallCoverImage;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    public String getLargeCoverImage() {
        return largeCoverImage;
    }

    public void setLargeCoverImage(String largeCoverImage) {
        this.largeCoverImage = largeCoverImage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }
}
