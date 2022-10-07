package com.nextory.testapp.data.book

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun observePagedBooks(): PagingSource<Int, Book>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBook(id: Long): Book
}