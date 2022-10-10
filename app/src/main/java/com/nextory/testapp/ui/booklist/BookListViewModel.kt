package com.nextory.testapp.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nextory.testapp.data.book.Book
import com.nextory.testapp.data.book.BookRepository
import com.nextory.testapp.data.favourite.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    private var observeBooksJob: Job? = null

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _pagedBooks = MutableStateFlow<PagingData<BookItem>>(PagingData.empty())
    val pagedBooks: StateFlow<PagingData<BookItem>> = _pagedBooks

    init {
        observeBooks()
    }

    fun onSearch(searchQuery: String) {
        _searchQuery.value = searchQuery
        observeBooks()
    }

    private fun observeBooks() {
        observeBooksJob?.cancel()
        observeBooksJob = viewModelScope.launch {
            bookRepository.observePagedBooks(PAGING_CONFIG, searchQuery.value)
                .cachedIn(viewModelScope)
                .combine(
                    favouriteRepository.favourites
                ) { books, favourites ->
                    books.map { book ->
                            BookItem(
                                book = book,
                                isFavourite = favourites.any { it.bookId == book.id }
                            )
                        }
                }.collect(_pagedBooks::emit)
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