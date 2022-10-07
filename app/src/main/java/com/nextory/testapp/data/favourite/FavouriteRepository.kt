package com.nextory.testapp.data.favourite

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val favouriteDao: FavouriteDao
) {
    val favourites: StateFlow<List<Favourite>> = favouriteDao
        .observeFavourites()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    suspend fun setFavourite(bookId: Long, isFavourite: Boolean) {
        withContext(Dispatchers.IO) {
            if (isFavourite) favouriteDao.addFavourite(Favourite(bookId))
            else favouriteDao.removeFavourite(Favourite(bookId))
        }
    }
}