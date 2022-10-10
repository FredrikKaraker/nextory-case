package com.nextory.testapp.data.book

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun observePagedBooks(
        pagingConfig: PagingConfig,
        searchQuery: String
    ): Flow<PagingData<Book>> {
        return Pager(config = pagingConfig) {
            bookDao.observePagedBooks("%${searchQuery.replace(" ", "%")}%")
        }.flow
    }

    suspend fun getBook(id: Long): Book =
        withContext(Dispatchers.IO) {
            bookDao.getBook(id)
        }
}