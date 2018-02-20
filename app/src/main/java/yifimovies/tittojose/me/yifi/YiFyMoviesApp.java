package yifimovies.tittojose.me.yifi;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by titto.jose on 20-02-2018.
 */

public class YiFyMoviesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
