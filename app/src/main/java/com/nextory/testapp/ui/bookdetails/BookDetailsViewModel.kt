package com.nextory.testapp.ui.bookdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextory.testapp.data.Book
import com.nextory.testapp.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState: MutableStateFlow<BookDetailsViewState> =
        MutableStateFlow(BookDetailsViewState.Loading)

    val viewState: StateFlow<BookDetailsViewState> = _viewState

    init {
        viewModelScope.launch {
            try {
                val bookId = checkNotNull(savedStateHandle.get<Long>("bookId"))
                val book = bookRepository.getBook(bookId)
                _viewState.update { BookDetailsViewState.Data(book) }
            } catch (e: Exception) {
                _viewState.update { BookDetailsViewState.Error }
            }
        }
    }
}

sealed class BookDetailsViewState {
    data class Data(val book: Book) : BookDetailsViewState()
    object Loading : BookDetailsViewState()
    object Error : BookDetailsViewState()
}