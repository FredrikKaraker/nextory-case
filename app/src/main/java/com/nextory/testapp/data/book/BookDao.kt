package com.nextory.testapp.data.book

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM book WHERE (author LIKE :searchQuery OR title LIKE :searchQuery)")
    fun observePagedBooks(searchQuery: String): PagingSource<Int, Book>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBook(id: Long): Book
}