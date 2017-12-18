package yifimovies.tittojose.me.yifi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MoviesAPIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static final String BASE_URL = "https://yts.am/api/";

    public static MoviesService getMoviesAPIService() {
        return MoviesAPIClient.getClient(BASE_URL).create(MoviesService.class);
    }
}
