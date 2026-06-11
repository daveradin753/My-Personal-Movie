package com.mypersonalmovie.di

import com.mypersonalmovie.data.local.dao.MovieDao
import com.mypersonalmovie.data.repository.MovieRepositoryImpl
import com.mypersonalmovie.data.source.MovieApiService
import com.mypersonalmovie.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieApiService: MovieApiService, movieDao: MovieDao): MovieRepository =
        MovieRepositoryImpl(movieApiService, movieDao)


}