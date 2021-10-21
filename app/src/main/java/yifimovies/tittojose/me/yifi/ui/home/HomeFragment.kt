package yifimovies.tittojose.me.yifi.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.fragment_home.*
import yifimovies.tittojose.me.yifi.R
import yifimovies.tittojose.me.yifi.bookmark.BookmarkActivity
import yifimovies.tittojose.me.yifi.genre.GenreActivity
import yifimovies.tittojose.me.yifi.search.SearchSuggestionActivity
import yifimovies.tittojose.me.yifi.ui.home.model.MovieListType
import yifimovies.tittojose.me.yifi.utils.NetworkUtils
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        setupToolbar()
        setupNavigationDrawer()
        setupViewPager(viewpager)
        if (!NetworkUtils.isNetworkConnected(context)) {
            showNetworkIssueMessage()
        } else {
            setupAppRatingPopup()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Search_Click")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search")
                mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                startActivity(Intent(requireActivity(), SearchSuggestionActivity::class.java))
                requireActivity().overridePendingTransition(0, 0)
            }
            android.R.id.home -> {
                drawer_layout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        (requireActivity() as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            try {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPager!!.offscreenPageLimit = 4
        val adapter = ViewPagerAdapter(parentFragmentManager)
//        adapter.addFragment(GenreListFragment(), "Genres")
        adapter.addFragment(MoviesListFragment.instance(MovieListType.LATEST), "Latest")
        adapter.addFragment(MoviesListFragment.instance(MovieListType.MOST_DOWNLOADED), "Popular")
        adapter.addFragment(MoviesListFragment.instance(MovieListType.TOP), "Top")
        viewPager.adapter = adapter
        viewPager.currentItem = 1
        tabs!!.setupWithViewPager(viewPager)
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

    private fun startRateAppIntent() {
        val uri = Uri.parse("market://details?id=${requireActivity().packageName}")
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
                    Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")))
        }
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "RateApp")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RateAppClicked")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
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

    private fun navigateGenreScreen() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "GenreScreenRedirect")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "GenreScreenRedirect")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        startActivity(Intent(requireActivity(), GenreActivity::class.java))
    }

    private fun navigateBookmarkScreen() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "BookmarkScreen")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "BookmarkScreen")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NavDrawer")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        startActivity(Intent(requireActivity(), BookmarkActivity::class.java))
    }

    private fun showNetworkIssueMessage() {
        val snackbar = Snackbar
                .make(coordinatorLayoutHome!!, "No Internet connection available. Please check your network connectivity and try again.", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun setupAppRatingPopup() {
        AppRate.with(requireActivity())
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
        AppRate.showRateDialogIfMeetsConditions(requireActivity())
    }
}