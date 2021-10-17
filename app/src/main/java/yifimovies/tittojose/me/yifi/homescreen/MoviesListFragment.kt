package yifimovies.tittojose.me.yifi.homescreen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yifimovies.tittojose.me.yifi.R
import yifimovies.tittojose.me.yifi.api.MoviesAPIClient
import yifimovies.tittojose.me.yifi.api.MoviesService
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse
import yifimovies.tittojose.me.yifi.homescreen.MoviesRecyclerAdapter.MoviesRecyclerAdapterListener
import yifimovies.tittojose.me.yifi.homescreen.model.MovieListType
import yifimovies.tittojose.me.yifi.moviedetailscreen.MovieDetailActivity
import java.util.*

/**
 * Created by titto.jose on 21-12-2017.
 */
class MoviesListFragment : Fragment() {
    @JvmField
    var moviesService: MoviesService? = null

    private var movies: MutableList<Any> = ArrayList()
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var isLoading = true

    @JvmField
    val PAGE_SIZE = 20

    @JvmField
    var page = 0
    var lastAdPosition = -1
    private var isLastPage = false
    var retryCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_main, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            page = 0
        }
        initializeMoviesList()
    }

    private fun initializeMoviesList() {
        swipeRefreshLayout!!.setOnRefreshListener {
            page = 0
            loadMovieData(page++)
        }
        val layoutManager = GridLayoutManager(activity, 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mAdapter!!.getItemViewType(position) == MoviesRecyclerAdapter.MOVIE) {
                    1
                } else -1
            }
        }
        recyclerViewMoviesList!!.layoutManager = layoutManager
        recyclerViewMoviesList!!.setHasFixedSize(true)
        recyclerViewMoviesList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                        loadMovieData(page++)
                    }
                }
            }
        })
        loadMovieData(page++)
    }

    private val recyclerAdapterListener = MoviesRecyclerAdapterListener { movie, imageView ->
        val options: ActivityOptions? = null
        val i = Intent(activity, MovieDetailActivity::class.java)
        i.putExtra("movie", movie)
        startActivity(i)
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    @JvmField
    var apiCallback: Callback<MovieAPIResponse> = object : Callback<MovieAPIResponse> {
        override fun onResponse(call: Call<MovieAPIResponse>, response: Response<MovieAPIResponse>) {
            try {
                if (response.isSuccessful) {
                    errorLayout!!.visibility = View.GONE
                    recyclerViewMoviesList!!.visibility = View.VISIBLE
                    progressPagination!!.visibility = View.GONE
                    isLoading = false
                    swipeRefreshLayout!!.isRefreshing = false
                    if (response.body()!!.data.movies != null && response.body()!!.data.movies.size > 0) {
                        if (page == 1) {
                            movies = ArrayList()
                            movies.addAll(response.body()!!.data.movies)
                            mAdapter = MoviesRecyclerAdapter(activity, movies, recyclerAdapterListener)
                            recyclerViewMoviesList!!.adapter = mAdapter
                            Log.d(TAG, "onResponse: ")
                            lastAdPosition = -1
                        } else {
                            movies.addAll(response.body()!!.data.movies)
                            mAdapter!!.notifyDataSetChanged()
                        }
                        val currentTotalItem = page * PAGE_SIZE
                        if (currentTotalItem >= response.body()!!.data.movieCount) {
                            isLastPage = true
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody().toString())
                    if (retryCount == 0) {
                        retryCount++
                        retryAPI()
                    } else {
                        try {
                            if (movies.size == 0) {
                                recyclerViewMoviesList!!.visibility = View.GONE
                                errorLayout!!.visibility = View.VISIBLE
                            }
                            swipeRefreshLayout!!.isRefreshing = false
                            (activity as HomeActivity?)!!.handleError("")
                        } catch (e: Exception) {
                            Log.e(TAG, "onResponse: $e")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onResponse: $e")
            }
        }

        override fun onFailure(call: Call<MovieAPIResponse>, t: Throwable) {
            if (retryCount == 0) {
                retryCount++
                retryAPI()
            } else {
                try {
                    if (movies.size == 0) {
                        recyclerViewMoviesList!!.visibility = View.GONE
                        errorLayout!!.visibility = View.VISIBLE
                    }
                    swipeRefreshLayout!!.isRefreshing = false
                    isLoading = false
                    progressPagination!!.visibility = View.GONE
                    (activity as HomeActivity?)!!.handleError("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadMovieData(page: Int) {
        isLoading = true
        if (page >= 1) {
            progressPagination!!.visibility = View.VISIBLE
        } else {
            swipeRefreshLayout!!.isRefreshing = true
        }
        moviesService = MoviesAPIClient.getMoviesAPIService()
        makeMoviesAPICall()
    }

    private fun retryAPI() {
        Log.d("OkHttp", "retryAPI: ")
        moviesService = MoviesAPIClient.getMoviesAPIFallbackService()
        makeMoviesAPICall()
    }

    private fun makeMoviesAPICall() {
        val movieType = arguments?.getParcelable<MovieListType>(KEY_MOVIE_LIST_TYPE)
        moviesService!!.getMovieList(movieType!!.query, page, PAGE_SIZE).enqueue(apiCallback)
    }

    companion object {
        private val TAG = MoviesListFragment::class.java.simpleName
        private const val KEY_MOVIE_LIST_TYPE = "movie_list_type"

        fun instance(movieListType: MovieListType): MoviesListFragment {
            return MoviesListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MOVIE_LIST_TYPE, movieListType)
                }
            }
        }
    }
}