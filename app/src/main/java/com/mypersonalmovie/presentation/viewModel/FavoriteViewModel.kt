package com.mypersonalmovie.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    private var _error = Channel<String?>()
    val error get() = _error.receiveAsFlow()

    private var _listFavorite = Channel<MutableList<MovieModel>>()
    val listFavorite get() = _listFavorite.receiveAsFlow()
    fun getAlllFavoriteMovie() = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getAllFavoriteMovies()
            .catch { e ->
                _error.send(e.message)
            }
            .collect {
                _listFavorite.send(it.toMutableList())
            }
    }

}