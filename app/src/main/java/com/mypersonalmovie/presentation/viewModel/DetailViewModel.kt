package com.mypersonalmovie.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mypersonalmovie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    fun getMovieReviews(movieId: Int) = movieRepository.getMovieReviews(movieId)
        .cachedIn(viewModelScope)

}