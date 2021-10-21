package yifimovies.tittojose.me.yifi.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import yifimovies.tittojose.me.yifi.data.MoviesRepository
import javax.inject.Inject

class MoviesListViewModel @ViewModelInject constructor(
        private val moviesRepository: MoviesRepository
) : ViewModel() {

}