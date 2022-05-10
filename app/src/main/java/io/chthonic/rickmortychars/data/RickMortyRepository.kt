package io.chthonic.rickmortychars.data

import androidx.paging.*
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RickMortyRepository @Inject constructor(
    private val characterResultRemoteMediator: CharacterResultRemoteMediator,
    private val charactersDao: CharactersDao
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getCharacters(): Flow<PagingData<CharacterInfo>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = characterResultRemoteMediator
        ) {
            charactersDao.pagingSource()
        }.flow.map { pagingData ->
            pagingData.filter {
                it.id != null && !it.image.isNullOrEmpty()
            }.map {
                CharacterInfo(
                    id = requireNotNull(it.id),
                    name = it.name ?: "",
                    image = requireNotNull(it.image)
                )
            }
        }
}