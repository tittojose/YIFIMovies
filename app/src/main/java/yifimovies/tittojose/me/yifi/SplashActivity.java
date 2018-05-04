package yifimovies.tittojose.me.yifi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import yifimovies.tittojose.me.yifi.homescreen.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        getDataEndPointData();
    }

    private void getDataEndPointData() {

        if (BuildConfig.DEBUG) {

            startHomeActivity();
        } else {
            setConstantValuesFromFirebaseRemote();
            long cacheExpiration = 900;

            mFirebaseRemoteConfig.fetch(cacheExpiration)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.activateFetched();
                            }
                            setConstantValuesFromFirebaseRemote();
                            startHomeActivity();
                        }
                    });
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setConstantValuesFromFirebaseRemote() {
        Constants.BASE_URL = mFirebaseRemoteConfig.getString(Constants.BASE_URL_KEY);
        Constants.TORRENT_APP_LINK = mFirebaseRemoteConfig.getString(Constants.TORRENT_APP_LINK_KEY);
        Constants.FALLBACK_BASE_URL = mFirebaseRemoteConfig.getString(Constants.FALLBACK_BASE_URL_KEY);
    }
}
