package com.mypersonalmovie.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    fun getMovieReviews(movieId: Int) = movieRepository.getMovieReviews(movieId)
        .cachedIn(viewModelScope)

    var _error = MutableStateFlow<String?>(null)
    val error get() = _error.asStateFlow()

    var _loading = Channel<Boolean>()
    val loading get() = _loading.receiveAsFlow()


    var _insertFavoriteMovie = Channel<String>()
    val insertFavoriteMovie get() = _insertFavoriteMovie.receiveAsFlow()
    fun insertFavoriteMovie(movie: MovieModel) = viewModelScope.launch(Dispatchers.IO) {
        _loading.send(true)
        movieRepository.insertFavoriteMovie(movie)
            .catch { e ->
                _error.value = e.message
                _loading.send(false)
            }
            .collect { data ->
                _insertFavoriteMovie.send(data)
                _loading.send(false)
            }
    }

    var _deleteFavoriteMovie = Channel<String>()
    val deleteFavoriteMovie get() = _deleteFavoriteMovie.receiveAsFlow()
    fun deleteFavoriteMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _loading.send(true)
        movieRepository.deleteFavoriteMovie(movieId)
            .catch { error ->
                _error.value = error.message
                _loading.send(false)
            }
            .collect { data ->
                _deleteFavoriteMovie.send(data)
                _loading.send(false)
            }
    }

    private var _isFavorite = MutableStateFlow(false)
    val isFavorite get() = _isFavorite.asStateFlow()

    fun isFavoriteMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.isFavoriteMovie(movieId).collect {
            _isFavorite.value = it
        }
    }

}