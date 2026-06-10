package com.mypersonalmovie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mypersonalmovie.data.dtos.MovieDetail
import com.mypersonalmovie.data.paging.MoviePagingSource
import com.mypersonalmovie.data.paging.ReviewPagingSource
import com.mypersonalmovie.data.source.MovieApiService
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.domain.model.ReviewModel
import com.mypersonalmovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService
): MovieRepository {

    override fun getPopularMovies(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, "popular")
            }
        ).flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, "top_rated")
            }
        ).flow
    }

    override fun getNowPlayingMovies(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, "now_playing")
            }
        ).flow
    }

    override fun getMovieDetail(movieId: Int): Flow<MovieDetail.Response> = flow {
        val response = movieApiService.getMovieDetail(movieId)
        emit(response)
    }

    override fun getMovieReviews(movieId: Int): Flow<PagingData<ReviewModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ReviewPagingSource(movieApiService, movieId)
            }
        ).flow
    }

}