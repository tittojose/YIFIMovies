package yifimovies.tittojose.me.yifi;

import androidx.multidex.MultiDexApplication;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Created by titto.jose on 20-02-2018.
 */

@HiltAndroidApp
public class MoviesApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
