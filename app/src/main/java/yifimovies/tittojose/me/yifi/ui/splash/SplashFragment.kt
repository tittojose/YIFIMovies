package yifimovies.tittojose.me.yifi.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import yifimovies.tittojose.me.yifi.BuildConfig
import yifimovies.tittojose.me.yifi.Constants
import yifimovies.tittojose.me.yifi.R

class SplashFragment : Fragment(R.layout.activity_splash) {
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .build()
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        getDataEndPointData()
    }


    private fun getDataEndPointData() {
        if (!BuildConfig.DEBUG) {
            startHomeActivity()
        } else {
            setConstantValuesFromFirebaseRemote()
            val cacheExpiration: Long = 900
            mFirebaseRemoteConfig!!.fetch(cacheExpiration)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            mFirebaseRemoteConfig!!.activate()
                        }
                        setConstantValuesFromFirebaseRemote()
                        startHomeActivity()
                    }
        }
    }

    private fun startHomeActivity() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun setConstantValuesFromFirebaseRemote() {
        Constants.BASE_API_END_POINT = mFirebaseRemoteConfig!!.getString(Constants.BASE_API_END_POINT_KEY)
        Constants.PRIMARY_API_END_POINT = mFirebaseRemoteConfig!!.getString(Constants.PRIMARY_API_END_POINT_KEY)
        Constants.TORRENT_APP_LINK = mFirebaseRemoteConfig!!.getString(Constants.TORRENT_APP_LINK_KEY)
    }
}