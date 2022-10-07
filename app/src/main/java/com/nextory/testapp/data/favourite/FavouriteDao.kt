package com.nextory.testapp.data.favourite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite")
    fun observeFavourites(): Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFavourite(favourite: Favourite)

    @Delete
    fun removeFavourite(favourite: Favourite)
}