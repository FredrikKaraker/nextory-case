package com.nextory.testapp.ui.booklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.nextory.testapp.R
import com.nextory.testapp.ui.components.ListItem
import com.nextory.testapp.ui.utils.rememberFlowWithLifecycle
import com.nextory.testapp.ui.utils.rememberStateWithLifecycle

@Composable
fun BookList(
    bookListViewModel: BookListViewModel = hiltViewModel(),
    onBookClicked: (Long) -> Unit
) {
    val pagedBooks = rememberFlowWithLifecycle(bookListViewModel.pagedBooks)
        .collectAsLazyPagingItems()

    val searchQuery by rememberStateWithLifecycle(bookListViewModel.searchQuery)
    BookList(
        bookItems = pagedBooks,
        searchText = searchQuery,
        onSearchTextChanged = bookListViewModel::onSearch,
        onBookClicked = onBookClicked
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun BookList(
    bookItems: LazyPagingItems<BookItem>,
    searchText: String,
    onSearchTextChanged: (String) -> Unit = {},
    onBookClicked: (Long) -> Unit
) {
    val listState = rememberLazyListState()
    Scaffold(topBar = { BookListTopBar() }) { paddingValues ->
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(paddingValues),
            contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                .asPaddingValues()
        ) {
            stickyHeader {
                val focusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = searchText,
                    onValueChange = {
                        onSearchTextChanged(it)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.search_placeholder))
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchText.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = { onSearchTextChanged("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors()
                )
            }

            items(
                items = bookItems,
                key = { it.book.id }
            ) { bookItem ->
                BookItem(
                    bookItem = bookItem!!,
                    onClicked = {
                        keyboardController?.hide()
                        onBookClicked(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun BookListTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.booklist_title)) },
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            )
        )
    )
}

@Composable
private fun BookItem(
    bookItem: BookItem,
    onClicked: (Long) -> Unit
) {
    val book = bookItem.book
    ListItem(
        modifier = Modifier.clickable { onClicked(book.id) },
        icon = {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        secondaryText = { Text(book.author) },
        trailing = {
            if (bookItem.isFavourite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null
                )
            }
        }
    ) {
        Text(book.title)
    }
}