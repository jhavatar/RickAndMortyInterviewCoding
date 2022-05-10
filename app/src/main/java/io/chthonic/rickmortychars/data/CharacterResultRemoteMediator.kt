package io.chthonic.rickmortychars.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.Lazy
import io.chthonic.rickmortychars.data.api.RickMortyApi
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.data.database.RickMortyDatabase
import io.chthonic.rickmortychars.data.model.CharacterResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val MAX_RETRY_COUNT = 3
const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
class CharacterResultRemoteMediator @Inject constructor(
    private val database: RickMortyDatabase,
    private val api: Lazy<RickMortyApi>,
    private val charactersDao: CharactersDao
) : RemoteMediator<Int, CharacterResult>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterResult>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.REFRESH -> 1
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.id?.let { it / PAGE_SIZE + 1 } ?: 1
                }
            }

            val response = getCharacters(page)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    charactersDao.clearAll()
                }
                charactersDao.insertAll(response)
            }
            MediatorResult.Success(
                endOfPaginationReached = response.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getCharacters(pageNumber: Int): List<CharacterResult> =
        retryIO(times = MAX_RETRY_COUNT) {
            api.get().getCharacters(pageNumber).results
        }
}
