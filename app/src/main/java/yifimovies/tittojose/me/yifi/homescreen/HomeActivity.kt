package yifimovies.tittojose.me.yifi.homescreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import yifimovies.tittojose.me.yifi.homescreen.MoviesListFragment.Companion.instance
import butterknife.BindView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.analytics.FirebaseAnalytics
import butterknife.ButterKnife
import yifimovies.tittojose.me.yifi.utils.NetworkUtils
import com.google.android.material.snackbar.Snackbar
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_home.*
import yifimovies.tittojose.me.yifi.R
import yifimovies.tittojose.me.yifi.bookmark.BookmarkActivity
import yifimovies.tittojose.me.yifi.genre.GenreActivity
import yifimovies.tittojose.me.yifi.homescreen.genre.GenreListFragment
import yifimovies.tittojose.me.yifi.homescreen.model.MovieListType.*
import yifimovies.tittojose.me.yifi.search.SearchSuggestionActivity
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        try {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setupNavigationDrawer()
        setupViewPager(viewpager)
        if (!NetworkUtils.isNetworkConnected(applicationContext)) {
            val snackbar = Snackbar
                    .make(coordinatorLayoutHome!!, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG)
            snackbar.show()
        } else {
            AppRate.with(this)
                    .setInstallDays(3) // default 10, 0 means install day.
                    .setLaunchTimes(5) // default 10
                    .setRemindInterval(5) // default 1
                    .setShowLaterButton(true) // default true
                    .setDebug(false) // default false
                    .setOnClickButtonListener { which ->

                        // callback listener.
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RateAppClicked")
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PromptRateApp")
                        if (which == -3) {
                            //remind
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "RemindToRate")
                        } else if (which == -2) {
                            // no thanks clicked
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "NoThanks")
                        } else {
                            // rate now
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "RateNow")
                        }
                        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                    }
                    .monitor()

            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(this)
        }
    }

    private fun setupNavigationDrawer() {
        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_rate_app -> startRateAppIntent()
                R.id.drawer_share_app -> startShareAppIntent()
                R.id.drawer_genre -> navigateGenreScreen()
                R.id.drawer_bookmark -> navigateBookmarkScreen()
            }
            drawer_layout.closeDrawers()
            true
        }
    }

    private fun navigateBookmarkScreen() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "BookmarkScreen")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "BookmarkScreen")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        startActivity(Intent(this@HomeActivity, BookmarkActivity::class.java))
    }

    private fun navigateGenreScreen() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "GenreScreenRedirect")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "GenreScreenRedirect")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        startActivity(Intent(this@HomeActivity, GenreActivity::class.java))
    }

    private fun startShareAppIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "YIFY Movies.")
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.shared_via))
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ShareApp")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ShareAppClicked")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun startRateAppIntent() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "RateApp")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RateAppClicked")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPager!!.offscreenPageLimit = 4
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(GenreListFragment(), "Genres")
        adapter.addFragment(instance(LATEST), "Latest")
        adapter.addFragment(instance(MOST_DOWNLOADED), "Popular")
        adapter.addFragment(instance(TOP), "Top")
        viewPager.adapter = adapter
        viewPager.currentItem = 1
        tabs!!.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Search_Click")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search")
                mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                startActivity(Intent(this@HomeActivity, SearchSuggestionActivity::class.java))
                overridePendingTransition(0, 0)
            }
            android.R.id.home -> {
                drawer_layout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun handleError(responseBody: String?) {
        val snackbar = Snackbar
                .make(coordinatorLayoutHome!!, "Network error. Please try again.", Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}