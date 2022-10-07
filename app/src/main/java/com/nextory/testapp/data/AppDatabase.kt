package com.nextory.testapp.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.nextory.testapp.data.book.Book
import com.nextory.testapp.data.book.BookDao
import com.nextory.testapp.data.favourite.Favourite
import com.nextory.testapp.data.favourite.FavouriteDao

@Database(
    version = 2,
    entities = [
        Book::class,
        Favourite::class
    ],
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun favouriteDao(): FavouriteDao
}