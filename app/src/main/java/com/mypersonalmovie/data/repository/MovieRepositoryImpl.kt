package com.mypersonalmovie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mypersonalmovie.data.dtos.MovieDetail
import com.mypersonalmovie.data.local.dao.MovieDao
import com.mypersonalmovie.data.local.entity.MovieEntity
import com.mypersonalmovie.data.paging.MoviePagingSource
import com.mypersonalmovie.data.paging.ReviewPagingSource
import com.mypersonalmovie.data.source.MovieApiService
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.domain.model.ReviewModel
import com.mypersonalmovie.domain.repository.MovieRepository
import com.mypersonalmovie.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao
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

    override fun insertFavoriteMovie(movie: MovieModel): Flow<String> = flow {
        try {
            movieDao.insertMovie(movie.toEntity())
            emit("Success adding to favorite")
        } catch (e: Exception) {
            throw e
        }
    }

    override fun deleteFavoriteMovie(movieId: Int): Flow<String> = flow {
        try {
            val movieEntity = movieDao.getMovieById(movieId)
            if (movieEntity != null) {
                movieDao.deleteMovie(movieEntity)
                emit("Success removing from favorite")
            } else {
                emit("Movie not found in favorites")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun isFavoriteMovie(movieId: Int): Flow<Boolean> {
        return movieDao.isMovieExists(movieId)
    }

}
