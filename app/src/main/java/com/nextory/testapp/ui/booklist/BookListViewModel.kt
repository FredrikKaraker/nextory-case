package com.nextory.testapp.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import androidx.paging.map
import com.nextory.testapp.data.book.Book
import com.nextory.testapp.data.book.BookRepository
import com.nextory.testapp.data.favourite.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    bookRepository: BookRepository,
    favouriteRepository: FavouriteRepository
) : ViewModel() {
    val pagedBooks =
        bookRepository.observePagedBooks(PAGING_CONFIG)
            .combine(favouriteRepository.favourites) { books, favourites ->
                books.map { book ->
                    BookItem(book, favourites.any { it.bookId == book.id })
                }
            }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 12,
            enablePlaceholders = false
        )
    }
}

data class BookItem(val book: Book, val isFavourite: Boolean)