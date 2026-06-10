package com.mypersonalmovie.domain.repository

import androidx.paging.PagingData
import com.mypersonalmovie.data.dtos.MovieDetail
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.domain.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getPopularMovies(): Flow<PagingData<MovieModel>>
    fun getTopRatedMovies(): Flow<PagingData<MovieModel>>
    fun getNowPlayingMovies(): Flow<PagingData<MovieModel>>
    fun getMovieDetail(movieId: Int): Flow<MovieDetail.Response>
    fun getMovieReviews(movieId: Int): Flow<PagingData<ReviewModel>>

}