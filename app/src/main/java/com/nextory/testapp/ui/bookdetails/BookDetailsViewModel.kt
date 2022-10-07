package com.nextory.testapp.ui.bookdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextory.testapp.data.book.Book
import com.nextory.testapp.data.book.BookRepository
import com.nextory.testapp.data.favourite.FavouriteRepository
import com.nextory.testapp.ui.utils.rethrowCancellation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val favouriteRepository: FavouriteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState: MutableStateFlow<BookDetailsViewState> =
        MutableStateFlow(BookDetailsViewState.Loading)

    val viewState: StateFlow<BookDetailsViewState> = _viewState

    lateinit var book: Book

    init {
        viewModelScope.launch {
            try {
                val bookId = checkNotNull(savedStateHandle.get<Long>("bookId"))
                book = bookRepository.getBook(bookId)
                favouriteRepository.favourites.collect { favourites ->
                    _viewState.update {
                        val isFavourite = favourites.any { it.bookId == bookId }
                        BookDetailsViewState.Success(book, isFavourite)
                    }
                }
            } catch (e: Exception) {
                e.rethrowCancellation()
                _viewState.update { BookDetailsViewState.Error }
            }
        }
    }

    fun setFavourite(bookId: Long, isFavourite: Boolean) {
        viewModelScope.launch {
            favouriteRepository.setFavourite(bookId, isFavourite)
        }
    }
}

sealed class BookDetailsViewState {
    data class Success(val book: Book, val isFavourite: Boolean) : BookDetailsViewState()
    object Loading : BookDetailsViewState()
    object Error : BookDetailsViewState()
}