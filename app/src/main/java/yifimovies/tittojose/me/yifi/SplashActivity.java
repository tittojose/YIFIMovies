package yifimovies.tittojose.me.yifi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        getDataEndPointData();
    }

    private void getDataEndPointData() {

        if (!BuildConfig.DEBUG) {

            startHomeActivity();
        } else {
            setConstantValuesFromFirebaseRemote();
            long cacheExpiration = 900;

            mFirebaseRemoteConfig.fetch(cacheExpiration)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.activate();
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
        Constants.BASE_API_END_POINT = mFirebaseRemoteConfig.getString(Constants.BASE_API_END_POINT_KEY);
        Constants.PRIMARY_API_END_POINT = mFirebaseRemoteConfig.getString(Constants.PRIMARY_API_END_POINT_KEY);
        Constants.TORRENT_APP_LINK = mFirebaseRemoteConfig.getString(Constants.TORRENT_APP_LINK_KEY);
    }
}
