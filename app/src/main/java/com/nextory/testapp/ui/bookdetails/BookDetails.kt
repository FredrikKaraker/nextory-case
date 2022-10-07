package com.nextory.testapp.ui.bookdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nextory.testapp.R
import com.nextory.testapp.data.book.Book
import com.nextory.testapp.ui.theme.TestAppTheme
import com.nextory.testapp.ui.utils.rememberStateWithLifecycle

@Composable
fun BookDetails(
    onBackClicked: () -> Unit,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    val viewState by rememberStateWithLifecycle(viewModel.viewState)
    BookDetails(
        viewState = viewState,
        onBackClicked = onBackClicked,
        setFavourite = viewModel::setFavourite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetails(
    viewState: BookDetailsViewState,
    onBackClicked: () -> Unit,
    setFavourite: (Long, Boolean) -> Unit
) {
    val successState = (viewState as? BookDetailsViewState.Success)
    val title = successState?.book?.title ?: ""
    val isFavourite = successState?.isFavourite == true
    val bookId = successState?.book?.id
    Scaffold(
        topBar = {
            SmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.smallTopAppBarColors(actionIconContentColor = Color.Unspecified),
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_content_description)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            bookId?.let { setFavourite(it, !isFavourite) }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(
                                id = if (isFavourite) R.string.favourite_content_description
                                else R.string.not_favourite_content_description
                            )
                        )
                    }
                }
            )
        },
    ) {
        when (viewState) {
            is BookDetailsViewState.Success -> Details(book = viewState.book)
            BookDetailsViewState.Error -> Error()
            BookDetailsViewState.Loading -> Loading()
        }
    }
}

@Composable
private fun Details(book: Book) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AsyncImage(
            model = book.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = book.author, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = book.description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun Error() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Something went wrong...")
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailsPreview() {
    TestAppTheme {
        BookDetails(
            BookDetailsViewState.Success(
                Book(
                    id = 0,
                    title = "The Devil and the Dark Water",
                    author = "Stuart Turton",
                    description = """
                It's 1634 and Samuel Pipps, the world's greatest detective, is being transported to Amsterdam to be executed for a crime he may, or may not, have committed. Traveling with him is his loyal bodyguard, Arent Hayes, who is determined to prove his friend innocent.

                But no sooner are they out to sea than devilry begins to blight the voyage. A twice-dead leper stalks the decks. Strange symbols appear on the sails. Livestock is slaughtered.
            """.trimIndent(),
                    imageUrl = ""
                ),
                isFavourite = false
            ),
            onBackClicked = {},
            setFavourite = { _, _ -> }
        )
    }
}


