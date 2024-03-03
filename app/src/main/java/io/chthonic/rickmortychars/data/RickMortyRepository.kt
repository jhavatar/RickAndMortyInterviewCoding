package io.chthonic.rickmortychars.data

import androidx.paging.*
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.data.database.models.CharacterInfoDb
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
            it.toCharacterInfo()
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
            pagingData.map {
                it.toCharacterInfo()
            }
        }

    private fun CharacterInfoDb.toCharacterInfo() =
        CharacterInfo(
            id = id,
            name = name,
            image = image
        )
}