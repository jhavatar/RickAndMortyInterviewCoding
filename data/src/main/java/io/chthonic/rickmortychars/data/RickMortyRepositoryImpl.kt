package io.chthonic.rickmortychars.data

import androidx.paging.*
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.data.database.models.CharacterInfoDb
import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import io.chthonic.rickmortychars.domain.dataapi.models.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RickMortyRepositoryImpl @Inject constructor(
    private val characterResultRemoteMediator: CharacterResultRemoteMediator,
    private val charactersDao: CharactersDao
) : RickMortyRepository {

    override suspend fun getCharacter(characterId: Int): CharacterInfo? =
        charactersDao.getCharacter(characterId)?.let {
            it.toDomainModel()
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(): Flow<PagingData<CharacterInfo>> =
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
                it.toDomainModel()
            }
        }

    private fun CharacterInfoDb.toDomainModel() =
        CharacterInfo(
            id = id,
            name = name,
            image = image
        )
}