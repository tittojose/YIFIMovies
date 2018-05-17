package yifimovies.tittojose.me.yifi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;

import static yifimovies.tittojose.me.yifi.Constants.BASE_URL;
import static yifimovies.tittojose.me.yifi.Constants.FALLBACK_BASE_URL;

/**
 * Created by titto.jose on 14-12-2017.
 */

public class MoviesAPIClient {
    private static Retrofit retrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            GsonBuilder builder = new GsonBuilder()
                    .registerTypeAdapter(MovieAPIResponse.class, new RestrictedContentFilter());

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory( GsonConverterFactory.create( builder.create()))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    private static Retrofit getFallbackClient(String baseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(MovieAPIResponse.class, new RestrictedContentFilter())
                   /* .registerTypeAdapter(Schedule.class, new CustomDeserializer())
                    .registerTypeAdapter(CallingParty.class, new CustomDeserializer())*/;


        GsonConverterFactory.create( builder.create());
//
//        GsonConverterFactory converter = new GsonConverterFactory(gson);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory( GsonConverterFactory.create( builder.create()))
                .client(client)
                .build();

        return retrofit;
    }


    public static MoviesService getMoviesAPIService() {
        return MoviesAPIClient.getClient(BASE_URL).create(MoviesService.class);
    }

    public static MoviesService getMoviesAPIFallbackService() {
        return MoviesAPIClient.getFallbackClient(FALLBACK_BASE_URL).create(MoviesService.class);
    }
}
