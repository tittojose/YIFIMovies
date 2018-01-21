package yifimovies.tittojose.me.yifi.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by titto.jose on 21-01-2018.
 */

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
