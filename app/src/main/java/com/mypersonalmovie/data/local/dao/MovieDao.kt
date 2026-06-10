package com.mypersonalmovie.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mypersonalmovie.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM movie_entities WHERE id = :id)")
    fun isMovieExists(id: Int): Flow<Boolean>

    @Query("SELECT * FROM movie_entities WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?
}
