package com.mypersonalmovie.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mypersonalmovie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    fun getPopularMovies() = movieRepository.getPopularMovies()
        .cachedIn(viewModelScope)

    fun getTopRatedMovies() = movieRepository.getTopRatedMovies()
        .cachedIn(viewModelScope)

    fun getNowPlayingMovies() = movieRepository.getNowPlayingMovies()
        .cachedIn(viewModelScope)

}