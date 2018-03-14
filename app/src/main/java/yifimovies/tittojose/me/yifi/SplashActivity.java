package yifimovies.tittojose.me.yifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        getDataEndPointData();
    }

    private void getDataEndPointData() {
        Constants.BASE_URL = mFirebaseRemoteConfig.getString(Constants.BASE_URL_KEY);
        long cacheExpiration = 3600;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Fetch Success",
                                    Toast.LENGTH_SHORT).show();
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(SplashActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Constants.BASE_URL = mFirebaseRemoteConfig.getString(Constants.BASE_URL_KEY);
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                });
    }
}
