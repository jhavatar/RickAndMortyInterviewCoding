package io.chthonic.rickmortychars.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.Lazy
import io.chthonic.rickmortychars.data.api.RickMortyApi
import io.chthonic.rickmortychars.data.model.CharacterResult
import io.chthonic.rickmortychars.domain.retryIO
import javax.inject.Inject

private const val MAX_RETRY_COUNT = 3

class CharacterResultPagingSource @Inject constructor(
    private val api: Lazy<RickMortyApi>
) : PagingSource<Int, CharacterResult>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterResult>): Int? =
        state.anchorPosition?.let { state.closestItemToPosition(it)?.id }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResult> {
        val pageNumber = params.key ?: 1
        return try {
            val response = getCharacters(pageNumber)
            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = if (response.isNotEmpty()) pageNumber + 1 else null
            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getCharacters(pageNumber: Int): List<CharacterResult> =
        retryIO(times = MAX_RETRY_COUNT) {
            api.get().getCharacters(pageNumber).results
        }
}