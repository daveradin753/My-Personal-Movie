package com.mypersonalmovie.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mypersonalmovie.data.local.dao.MovieDao
import com.mypersonalmovie.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}
