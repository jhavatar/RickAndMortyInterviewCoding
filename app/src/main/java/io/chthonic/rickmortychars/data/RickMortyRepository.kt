package io.chthonic.rickmortychars.data

import androidx.paging.*
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.data.models.CharacterResult
import io.chthonic.rickmortychars.domain.models.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RickMortyRepository @Inject constructor(
    private val characterResultRemoteMediator: CharacterResultRemoteMediator,
    private val charactersDao: CharactersDao
) {

    suspend fun getCharacter(characterId: Int): CharacterInfo? =
        charactersDao.getCharacter(characterId)?.let {
            it.toCharacterResult()
        }

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
                it.toCharacterResult()
            }
        }

    private fun CharacterResult.toCharacterResult() =
        CharacterInfo(
            id = requireNotNull(id),
            name = name ?: "",
            image = requireNotNull(image)
        )
}