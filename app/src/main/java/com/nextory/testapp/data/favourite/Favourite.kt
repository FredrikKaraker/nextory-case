package com.nextory.testapp.data.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class Favourite(
    @PrimaryKey val bookId: Long
)
